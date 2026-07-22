import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HmacSha256Example {

    public static void main(String[] args) {
        try {
            // 原始数据
            String message = "Hello, HMAC!";
            // 密钥（注意：实际应用中应从安全存储获取）
            String secretKey = "your-secret-key-12345";

            // 生成HMAC-SHA256签名
            String hmac = calculateHmac(message, secretKey);

            System.out.println("Message:  " + message);
            System.out.println("Secret:   " + secretKey);
            System.out.println("HMAC:     " + hmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String calculateHmac(String message, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        // 1. 获取HMAC-SHA256实例
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");

        // 2. 将密钥转换为字节数组
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        // 3. 初始化密钥规范
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

        // 4. 用密钥初始化Mac实例
        sha256Hmac.init(secretKeySpec);

        // 5. 计算消息的HMAC
        byte[] hmacBytes = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        // 6. 将字节数组转换为十六进制字符串
        return HexFormat.of().formatHex(hmacBytes);
    }

}