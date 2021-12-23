package ru.pshiblo.elgamal.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import ru.pshiblo.elgamal.core.crypto.service.CryptorService;
import ru.pshiblo.elgamal.core.crypto.service.DecryptorService;
import ru.pshiblo.elgamal.core.crypto.service.impl.CryptorServiceImpl;
import ru.pshiblo.elgamal.core.crypto.service.impl.DecryptorServiceImpl;
import ru.pshiblo.elgamal.core.crypto.key.GeneratorKey;

/**
 * @author Maxim Pshiblo
 */
public class CryptoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GeneratorKey.class).annotatedWith(Names.named("Key")).to(GeneratorKey.class).in(Scopes.SINGLETON);
        bind(CryptorService.class).to(CryptorServiceImpl.class);
        bind(DecryptorService.class).to(DecryptorServiceImpl.class);
    }
}
