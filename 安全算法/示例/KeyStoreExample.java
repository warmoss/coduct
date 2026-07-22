import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyStoreExample {

    public static void main(String[] args) throws Exception {
        // 1. 创建或加载KeyStore
        KeyStore keyStore = createOrLoadKeyStore();

        // 2. 生成并存储对称密钥(AES)
        storeSymmetricKey(keyStore);

        // 3. 加载并存储证书
        storeCertificate(keyStore);

        // 4. 保存KeyStore到文件
        saveKeyStore(keyStore, "keystore.p12", "storepass");

        // 5. 从文件加载KeyStore并读取内容
        loadAndReadKeyStore("keystore.p12", "storepass");
    }

    private static KeyStore createOrLoadKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        char[] password = "storepass".toCharArray();

        // 初始化新的KeyStore
        keyStore.load(null, password);
        return keyStore;
    }

    private static void storeSymmetricKey(KeyStore keyStore) throws Exception {
        // 生成AES密钥
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        // 创建密钥保护密码
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection("keypass".toCharArray());

        // 存储到KeyStore
        keyStore.setEntry("myAesKey", secretKeyEntry, protection);
        System.out.println("AES密钥已存储");
    }

    private static void storeCertificate(KeyStore keyStore) throws Exception {
        // 从文件加载证书 (需提前准备或生成)
        FileInputStream certFile = new FileInputStream("example.cer");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(certFile);

        // 存储证书
        keyStore.setCertificateEntry("serverCert", cert);
        System.out.println("证书已存储: " + cert.getSubjectDN());
    }

    private static void saveKeyStore(KeyStore keyStore, String filename, String password)
            throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            keyStore.store(fos, password.toCharArray());
        }
        System.out.println("KeyStore已保存到: " + filename);
    }

    private static void loadAndReadKeyStore(String filename, String password)
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(filename)) {
            keyStore.load(fis, password.toCharArray());
        }

        // 读取对称密钥
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection("keypass".toCharArray());
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry("myAesKey", protParam);
        SecretKey secretKey = secretKeyEntry.getSecretKey();
        System.out.println("读取的AES密钥: " + secretKey.getAlgorithm() + "/" + secretKey.getFormat());

        // 读取证书
        Certificate cert = keyStore.getCertificate("serverCert");
        System.out.println("读取的证书: " + ((X509Certificate) cert).getSubjectDN());

        // 列出所有别名
        System.out.println("\nKeyStore内容:");
        keyStore.aliases().asIterator()
                .forEachRemaining(alias -> {
                    try {
                        System.out.println("- " + alias +
                                " (类型: " + getEntryType(keyStore, alias) + ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private static String getEntryType(KeyStore ks, String alias) throws Exception {
        if (ks.isKeyEntry(alias))
            return "密钥条目";
        if (ks.isCertificateEntry(alias))
            return "证书条目";
        return "未知类型";
    }
}