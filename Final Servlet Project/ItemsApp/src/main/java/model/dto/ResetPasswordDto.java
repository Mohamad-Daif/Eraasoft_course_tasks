package model.dto;

public record ResetPasswordDto(String username,String currentPassword,String newPassword) {
}
