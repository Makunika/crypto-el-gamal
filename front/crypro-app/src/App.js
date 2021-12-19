import {Button, Col, Container, Form, Row, Tab, Tabs} from "react-bootstrap";
import React, {useState, useRef} from 'react';
import SockJsClient from 'react-stomp';
import axios from "axios";
import bigInt from "big-integer";

const usernames = ['Maxim1', 'Maxim2'];


function generateNewKey() {
    const p = generateRandomPrime();
    console.log(`[GENERATE KEY] p = ${p}`)
    const g = bigInt.randBetween(2, p.subtract(1))//bigInt(getPRoot(p));
    console.log(`[GENERATE KEY] g = ${g}`)
    const x = bigInt.randBetween(2, p.subtract(1));
    console.log(`[GENERATE KEY] x = ${x}`)
    const y = g.modPow(x, p);
    console.log(`[GENERATE KEY] y = ${y}`)

    const result = {p, g, x, y};

    return {
        p,
        g,
        x,
        y
    };
}

function encryptMsg(msg, openKey) {
    console.log(`[Encrypt] Encrypt ${msg}, open-key ${openKey.toString()}`);

    let a = "";
    let b = "";

    for (let i = 0; i < msg.length; i++) {
        let encryptNum = encryptNumber(msg.charCodeAt(i), openKey);
        a += toStringBigInteger(encryptNum.a) + ",";
        b += toStringBigInteger(encryptNum.b) + ",";
    }

    console.log(`[Encrypt] Result encrypt a = ${a}, b = ${b}`);
    return {
        allMessage: a + "!" + b,
        a: a,
        b: b
    };
}

function encryptNumber(msg, openKey) {
    console.log(`[Encrypt] Encrypt number ${msg}, open-key ${openKey.toString()}`);
    const sessionKey = bigInt.randBetween(2, openKey.p - 2);
    const a = openKey.g.modPow(sessionKey, openKey.p);
    const b = bigInt(msg).multiply(openKey.y.modPow(sessionKey, openKey.p)).mod(openKey.p);
    console.log(`[Encrypt] Result encrypt a = ${a.toString()}, b = ${b.toString()}`);

    return {
        a,
        b
    };
}

function toBigInt(str) {
    const array = Array.from(str, (char) => char.charCodeAt(0) - 48);
    return bigInt.fromArray(array, 74);
}

function decryptMsg(msg, secretKey) {
    const [aStr, bStr] = msg.split(/!/);
    console.log(`[Decrypt] Decrypt a = ${aStr}, b = ${bStr}. key ${secretKey.toString()}`);

    let result = "";
    const aLetters = aStr.split(/,/);
    const bLetters = bStr.split(/,/);

    for (let i = 0; i < aLetters.length - 1; i++) {
        let resultNum = decryptNumber(toBigInt(aLetters[i]), toBigInt(bLetters[i]), secretKey);
        result += String.fromCharCode(resultNum.toJSNumber());
    }

    console.log(`[Decrypt] Decrypt result = ${result}`);
    return result;
}

function decryptNumber(aCode, bCode, secretKey) {
    const a = bigInt(aCode);
    const b = bigInt(bCode);
    console.log(`[Decrypt] Decrypt a = ${a}, b = ${b}. key ${secretKey.toString()}`);
    const result = b.multiply(a.pow(secretKey.p.minus(1).minus(secretKey.x))).mod(secretKey.p)
    console.log(`[Decrypt] Decrypt result = ${result}`);
    return result;
}

function toStringBigInteger(number) {
    const values = number.toArray(74);
    let result = "";
    for (let i = 0; i < values.value.length; i++) {
        result += String.fromCharCode(values.value[i] + 48);
    }
    return result;
}

function getPRoot(p) {
    for (let i = bigInt(0); i.lesser(p); i.next()) {
        if (isPRoot(p, i)) {
            console.log("root = " + i)
            return i;
        }
    }
    return 0;
}

function isPRoot(p, root) {
    if (root.isZero() || root.eq(bigInt.one)) {
        return false;
    }
    let last = bigInt.one;
    const set = new Set();
    for (let i = bigInt(0); i.lesser(p.subtract(1)); i++) {
        last = last.multiply(root).mod(p);
        if (set.has(last)) {
            return false;
        }
        set.add(last);
    }
    return true;
}

function generateRandomPrime() {
    while (true) {
        let random = bigInt.randBetween("65536", "8388607");
        if (random.isPrime()) {
            return random;
        }
    }
}

function App() {
    const [username, setUsername] = useState(usernames[0]);
    const [usernameTo, setUsernameTo] = useState(usernames[1]);
    const [message, setMessage] = useState('');
    const [messageA, setMessageA] = useState('');
    const [messageB, setMessageB] = useState('');
    const [inMessage, setInMessage] = useState('');
    const [inDecryptMessage, setInDecryptMessage] = useState('');
    const [myKey, setMyKey] = useState({})
    const [isConnected, setIsConnected] = useState(false)
    const [openKey, setOpenKey] = useState({})
    const clientRef = useRef();

    const handleChangeMsg = (msg) => {
        let result = encryptMsg(msg.target.value, openKey);
        setMessage(result.allMessage);
        setMessageA(result.a);
        setMessageB(result.b);
    }

    const handleSend = (event) => {
        console.log(event)
        const msg = {
            username: usernameTo,
            message: message
        }
        clientRef.current.sendMessage("/app/chat", JSON.stringify(msg));
    }

    const handleShow = () => {
        console.log(openKey)
        console.log(myKey)
    }

    const handleCreateChat = () => {
        let newSecretKey = generateNewKey();

        const sendMsg = {
            usernameTo: usernameTo,
            usernameFrom: username,
            p: newSecretKey.p,
            g: newSecretKey.g,
            y: newSecretKey.y,
        }
        axios.post("http://localhost:8080/api/create-chat", sendMsg).then(r => setMyKey(newSecretKey))
    }

    const handleChangeUsername = () => {
        const tmp = username;
        setUsername(usernameTo)
        setUsernameTo(tmp)
    }

    return (
        <div>
            <SockJsClient url='http://localhost:8080/ws' topics={['/user/' + username + '/messages' ]}
                          onMessage={(msg) => {
                              console.log(msg)
                              console.log("chat")
                              const decryptMsg1 = decryptMsg(msg.message, myKey);
                              console.log(decryptMsg1)
                              setInMessage(msg.message)
                              setInDecryptMessage(decryptMsg1.toString())
                          }}
                          ref={clientRef} />
            <SockJsClient url='http://localhost:8080/ws' topics={['/user/' + username + '/create' ]}
                          onMessage={(msg) => {
                              console.log("create chat")
                              console.log(msg)
                              let newSecretKey = generateNewKey();
                              const sendMsg = {
                                  usernameTo: msg.usernameTo,
                                  usernameFrom: msg.usernameFrom,
                                  p: newSecretKey.p,
                                  g: newSecretKey.g,
                                  y: newSecretKey.y,
                              }
                              console.log(sendMsg)
                              axios.post("http://localhost:8080/api/create-chat/key", sendMsg
                              ).then(r => {
                                  setMyKey(newSecretKey);
                              });

                              setOpenKey({
                                  p: bigInt(msg.p),
                                  g: bigInt(msg.g),
                                  y: bigInt(msg.y),
                              });
                          }} />
            <SockJsClient url='http://localhost:8080/ws' topics={['/user/' + username + '/key' ]}
                          onMessage={(msg) => {
                              console.log("key")
                              console.log(msg);
                              setOpenKey({
                                  p: bigInt(msg.p),
                                  g: bigInt(msg.g),
                                  y: bigInt(msg.y),
                              });
                          }} />
            <Container fluid="md" className="my-5">
                <Row className="mb-5">
                    <Col>
                        <div>
                            Hello, {username}
                        </div>
                        <Button
                            variant="outline-primary"
                            size="sm"
                            onClick={handleChangeUsername}
                            disabled={isConnected}
                        >
                            Изменить пользователя
                        </Button>
                    </Col>
                </Row>
                <Row className="mb-2 bg-light border p-3" xs="auto">
                    <Col>
                        <div className="mb-2 fw-bold">Секретный (x) и открытый ключ (p, g, y) (мой)</div>
                        <Row xs="auto"><Col>x</Col><Col>{myKey.x?.toString()}</Col></Row>
                        <Row xs="auto"><Col>p</Col><Col>{myKey.p?.toString()}</Col></Row>
                        <Row xs="auto"><Col>g</Col><Col>{myKey.g?.toString()}</Col></Row>
                        <Row xs="auto"><Col>y</Col><Col>{myKey.y?.toString()}</Col></Row>
                    </Col>
                    <Col>
                        <div className="mb-2 fw-bold">Открытый ключ (p, g, y) (чужой)</div>
                        <Row xs="auto"><Col>p</Col><Col>{openKey.p?.toString()}</Col></Row>
                        <Row xs="auto"><Col>g</Col><Col>{openKey.g?.toString()}</Col></Row>
                        <Row xs="auto"><Col>y</Col><Col>{openKey.y?.toString()}</Col></Row>
                    </Col>
                </Row>
                <Row className="mb-2">
                    <Button
                        variant="primary"
                        onClick={handleCreateChat}
                    >
                        Соединить
                    </Button>
                </Row>
                <Row className="mb-2">
                    <Tabs defaultActiveKey="send" className="mb-3">
                        <Tab eventKey="send" title="Отправить">
                            <Form>
                                <Form.Group as={Row} className="mb-3" controlId="formHorizontalCheck">
                                    <Col>
                                        <Form.Control onChange={handleChangeMsg} type="text"  placeholder="Сообщение" />
                                    </Col>
                                </Form.Group>
                                <Form.Group as={Row} className="mb-3">
                                    <Col sm={10}>
                                        <Button type="button" onClick={handleSend}>Отправить</Button>
                                    </Col>
                                </Form.Group>
                            </Form>
                            <hr />
                            <Form.Group as={Row} className="mb-3" controlId="formHorizontalCheck">
                                <Form.Label column sm="2">
                                    Результат шифрования с помощью чужого открытого ключа (a)
                                </Form.Label>
                                <Col sm="10">
                                    <Form.Control type="text" placeholder="Результат шифрования (a)" readOnly value={messageA}/>
                                </Col>
                            </Form.Group>
                            <Form.Group as={Row} className="mb-3" controlId="formHorizontalCheck">
                                <Form.Label column sm="2">
                                    Результат шифрования с помощью чужого открытого ключа (b)
                                </Form.Label>
                                <Col sm="10">
                                    <Form.Control type="text" placeholder="Результат шифрования (b)" readOnly value={messageB}/>
                                </Col>
                            </Form.Group>
                        </Tab>
                        <Tab eventKey="exit" title="Входящие">
                            <Form.Group as={Row} className="mb-2" controlId="formHorizontalCheck">
                                <Form.Label column sm="2">
                                    Пришло сообщение
                                </Form.Label>
                                <Col sm="10">
                                    <Form.Control type="text" readOnly value={inMessage}/>
                                </Col>
                            </Form.Group>
                            <Form.Group as={Row} className="mb-2" controlId="formHorizontalCheck">
                                <Form.Label column sm="2">
                                    Расшифрованное сообщение при помощи моего секретного ключа
                                </Form.Label>
                                <Col sm="10">
                                    <Form.Control type="text" readOnly value={inDecryptMessage}/>
                                </Col>
                            </Form.Group>
                        </Tab>
                    </Tabs>
                </Row>
            </Container>
        </div>
    );
}

export default App;
