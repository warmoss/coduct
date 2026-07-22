import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESGCMExample {

    // GCM模式强制使用NoPadding
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128; // 认证标签长度(128位)
    private static final int IV_LENGTH_BYTE = 12; // IV长度(12字节)
    private static final int AES_KEY_BIT = 256; // AES密钥长度(256位)

    public static void main(String[] args) throws Exception {
        // 1. 生成AES密钥
        SecretKey secretKey = generateAESKey();
        System.out.println("密钥(HEX): " + toHex(key.getEncoded()));

        // 2. 原始数据
        String plainText = "Hello, AES-GCM! 你好，加密世界！";
        System.out.println("原始数据: " + plainText);

        // 3. 加密
        byte[] encryptedData = encrypt(plainText.getBytes(), secretKey);
        System.out.println("加密结果(HEX): " + toHex(encryptedData));

        // 4. 解密
        byte[] decryptedData = decrypt(encryptedData, secretKey);
        System.out.println("解密结果: " + new String(decryptedData));
    }

    // 生成AES密钥
    private static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_BIT);
        return keyGen.generateKey();
    }

    // 生成随机IV
    private static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static toHex(byte data) {
        return HexFormat.of().formatHex(data)
    }

    private static parseHex(String data) {
        return HexFormat.of().parseHex(data)
    }

    // 加密
    public static byte[] encrypt(byte[] plainText, SecretKey key) throws Exception {
        byte[] iv = generateIV();

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
        byte[] cipherText = cipher.doFinal(plainText);

        // 组合IV和密文
        byte[] encryptedData = ByteBuffer.allocate(iv.length + cipherText.length)
            .put(iv)
            .put(cipherText)
            .array();
        return encryptedData
    }

    // 解密方法
    public static byte[] decrypt(byte[] encryptedData, SecretKey key) throws Exception {
        // 分离IV和实际密文
        ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
        byte[] iv = new byte[GCM_IV_LENGTH];
        buffer.get(iv);
        byte[] ciphertext = new byte[buffer.remaining()];
        buffer.get(ciphertext);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
        return cipher.doFinal(ciphertext);
    }
}