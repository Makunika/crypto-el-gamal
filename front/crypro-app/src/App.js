import {Button, Col, Container, Row} from "react-bootstrap";
import React, {useState, useRef} from 'react';
import SockJsClient from 'react-stomp';
import axios from "axios";

const usernames = ['Maxim1', 'Maxim2'];


function generateNewKey() {
    const p = generateRandomPrime();
    console.log(`[GENERATE KEY] p = ${p}`)
    const g = getPRoot(p);
    console.log(`[GENERATE KEY] g = ${g}`)
    const x = randomInteger(2, p);
    console.log(`[GENERATE KEY] x = ${x}`)
    const y = Math.pow(g, x) % p;
    console.log(`[GENERATE KEY] y = ${y}`)

    return {
        p,
        g,
        x,
        y
    };
}

function encryptMsg(msg, openKey) {
    if (msg > openKey.p) {
        throw new Error("сообщение должно быть больше p")
    }
    console.log(`[Encrypt] Encrypt ${msg}, open-key ${openKey}`);
    const sessionKey = randomInteger(2, openKey.p - 1);
    const a = Math.pow(openKey.g, sessionKey) % openKey.p;
    const b = (Math.pow(openKey.y, sessionKey) * msg) % openKey.p;
    console.log(`[Encrypt] Result encrypt a = ${a}, b = ${b}, sum = ${a * 10 + b}`);
    return a * 10 + b;
}

function decryptMsg(msg, secretKey) {
    let a = msg / 10;
    let b = msg % 10;
    console.log(`[Decrypt] Decrypt a = ${a}, b = ${b}. key ${secretKey}`);
    const result = b * Math.pow(Math.pow(a, secretKey.x), -1) % secretKey.p;
    console.log(`[Decrypt] Decrypt result = ${result}`);
    return result;
}

function isPrime(p) {
    for (let i = 2; i < Math.sqrt(p); i++) {
        if (p % i !== 0) {
            return false
        }
    }
    return true
}

function getPRoot(p) {
    for (let i = 2; i < p; i++) {
        if (isPRoot(p, i)) {
            console.log("root = " + i)
            return i;
        }
    }
    return 0;
}

function isPRoot(p, root) {
    if (root === 0 || root === 1) {
        return false;
    }
    let last = 1;
    const set = new Set();
    for (let i = 0; i < p - 1; i++) {
        last = (last * root) % p;
        if (set.has(last)) {
            return false;
        }
        set.add(last);
    }
    return true;
}

function generateRandomPrime() {
    while (true) {
        let random = randomInteger(12, 100000);
        if (isPrime(random)) {
            return random;
        }
    }
}

function randomInteger(min, max) {
    let rand = min + Math.random() * (max + 1 - min);
    return Math.floor(rand);
}

function App() {
    const [username, setUsername] = useState(usernames[0]);
    const [usernameTo, setUsernameTo] = useState(usernames[1]);
    const [message, setMessage] = useState('');
    const [myKey, setMyKey] = useState(generateNewKey())
    const [isConnected, setIsConnected] = useState(false)
    const [openKey, setOpenKey] = useState({
        p: 0,
        g: 0,
        y: 0,
    })
    const clientRef = useRef();

    const handleChangeMsg = (msg) => {
        setMessage(encryptMsg(msg, myKey))
    }

    const handleSend = () => {
        const msg = {
            username: usernameTo,
            message: message
        }
        clientRef.current.sendMessage("/app/chat", JSON.stringify(msg));
    }

    const handleCreateChat = () => {
        setMyKey(generateNewKey());
        const sendMsg = {
            usernameTo: usernameTo,
            usernameFrom: username,
            p: myKey.p,
            g: myKey.g,
            y: myKey.y,
        }
        axios.post("http://localhost:8080/api/create-chat", sendMsg).then(r => console.log(r))
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
                              console.log(decryptMsg(msg.message, openKey))
                          }}
                          ref={clientRef} />
            <SockJsClient url='http://localhost:8080/ws' topics={['/user/' + username + '/create' ]}
                          onMessage={(msg) => {
                              console.log("create chat")
                              console.log(msg)
                              setMyKey(generateNewKey());
                              const sendMsg = {
                                  usernameTo: msg.usernameTo,
                                  usernameFrom: msg.usernameFrom,
                                  p: myKey.p,
                                  g: myKey.g,
                                  y: myKey.y,
                              }
                              console.log(sendMsg)
                              axios.post("http://localhost:8080/api/create-chat/key", sendMsg
                              ).then(r => {
                                  console.log(r)
                              });

                              setOpenKey({
                                  p: msg.p,
                                  g: msg.g,
                                  y: msg.y,
                              });
                          }} />
            <SockJsClient url='http://localhost:8080/ws' topics={['/user/' + username + '/key' ]}
                          onMessage={(msg) => {
                              console.log("key")
                              console.log(msg);
                              setOpenKey({
                                  p: msg.p,
                                  g: msg.g,
                                  y: msg.y,
                              });
                          }} />
            <Container fluid="md" className="my-5">
                <Row className="mb-2">
                    <Col>
                        <div>
                            Hello, {username}
                        </div>
                        <Button
                            variant="primary"
                            onClick={handleChangeUsername}
                            disabled={isConnected}
                        >
                            Изменить пользователя
                        </Button>
                    </Col>
                </Row>
                <Row className="mb-2">
                    <Col>
                        <Button
                            variant="primary"
                            onClick={handleCreateChat}
                        >
                            Соединить
                        </Button>
                    </Col>
                </Row>
                <Row className="mb-2">
                    <Col>
                        Hello
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default App;
