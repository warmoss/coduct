import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HexFormat;

public class RSAHexExample {
    private static final String ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    
    public static void main(String[] args) throws Exception {
        // 1. 生成RSA密钥对（4096位）
        KeyPair keyPair = generateRSAKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        String originalText = "Hello, RSA Encryption!";
        System.out.println("原始文本: " + originalText);
        
        // 2. 使用公钥加密
        byte[] encryptedBytes = encrypt(publicKey, originalText.getBytes());
        String hexEncrypted = bytesToHex(encryptedBytes);
        System.out.println("加密结果(Hex): " + hexEncrypted);
        
        // 3. 使用私钥解密
        byte[] decryptedBytes = decrypt(privateKey, hexToBytes(hexEncrypted));
        String decryptedText = new String(decryptedBytes);
        System.out.println("解密文本: " + decryptedText);
    }
    
    // 生成RSA密钥对
    private static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(4096); // 密钥长度
        return keyGen.generateKeyPair();
    }
    
    // RSA加密
    private static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }
    
    // RSA解密
    private static byte[] decrypt(PrivateKey privateKey, byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }
    
    // 字节数组转Hex字符串 (Java 17+)
    private static String bytesToHex(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);
    }
    
    // Hex字符串转字节数组 (Java 17+)
    private static byte[] hexToBytes(String hexString) {
        return HexFormat.of().parseHex(hexString);
    }
}