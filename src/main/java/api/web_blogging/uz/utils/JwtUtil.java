package api.web_blogging.uz.utils;

import api.web_blogging.uz.dto.JwtResponseDto;
import api.web_blogging.uz.enums.ProfileRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

public class JwtUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "fGq1vJxJ8mvV2kjJh78v5K2n6sY9xQzP3gKmRpF3zAs=";

    public static String encode(Integer id, List<ProfileRole> rolesList) {
        String strRoles = rolesList.stream().map(Enum::name)
                .collect(Collectors.joining(","));

        Map<String, String> claims = new HashMap<>();
        claims.put("roles", strRoles);

        return Jwts
                .builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static JwtResponseDto decode(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Integer id = Integer.valueOf(claims.getSubject());
        String strRoles = (String) claims.get("roles");
//        String [] roles = strRoles.split(",");

//        List<ProfileRole> rolesLists = new ArrayList<>();
//        for(String role : roles) {
//            rolesLists.add(ProfileRole.valueOf(role));
//        }

        List<ProfileRole> roleLists = Arrays.stream(strRoles.split(","))
                .map(ProfileRole::valueOf)
                .toList();


        return new JwtResponseDto(id, roleLists);
    }

    public static String encodeEmail(Integer id) {
        return Jwts
                .builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (60 * 60 * 10)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Integer decodeEmail(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Integer id = Integer.valueOf(claims.getSubject());
        return id;
    }

    //    public static JwtDTO decode(String token) {
//        Claims claims = Jwts
//                .parser()
//                .verifyWith(getSignInKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//        String username = (String) claims.get("username");
//        String role = (String) claims.get("role");
//        return new JwtDTO(username, role);
//    }
//
    private static SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
