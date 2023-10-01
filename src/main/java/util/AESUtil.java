package util;

import cn.hutool.core.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;


/**
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用
 * 数据库aes加密解密
 * -- 加密
 *    SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 *    -- 解密
 *    SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 * @author 836508
 *
 */
public class AESUtil {

    // 加密
    private static final String SECRET = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES加密ECB模式PKCS7Padding填充方式
     * @param str 字符串
     * @param key 密钥
     * @return 加密字符串
     * @throws Exception 异常信息
     */
    public static String aes256ECBPkcs7PaddingEncrypt(String str, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, SECRET));
        byte[] doFinal = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.encode(doFinal);
    }



}