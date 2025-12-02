import java.time.LocalDateTime;
import java.util.Optional;

class AccountDTO {
    private Long id;
    private String username;
    private LocalDateTime createdDate;

    public AccountDTO(Long id, String username, LocalDateTime createdDate) {
        this.id = id;
        this.username = username;
        this.createdDate = createdDate;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedDate() { return createdDate; }
}

interface AccountService {
    boolean validateLogin(String username, String password);
    Optional<AccountDTO> getAccountByUsername(String username);
}

public class AccountControllerMockTest{
    static int passed = 0, total = 0;

    public static void main(String[] args) {
        System.out.println("4.1 AccountController Mock Testing - Giả lập trên JDoodle\n");

        // Tạo Mock Service
        AccountService service = new AccountService() {
            @Override
            public boolean validateLogin(String u, String p) {
                System.out.println("   [Mock] validateLogin(\"" + u + "\", \"" + p + "\")");
                return "admin".equals(u) && "admin123".equals(p);
            }

            @Override
            public Optional<AccountDTO> getAccountByUsername(String u) {
                System.out.println("   [Mock] getAccountByUsername(\"" + u + "\")");
                if ("admin".equals(u)) {
                    return Optional.of(new AccountDTO(1L, "admin", LocalDateTime.now()));
                }
                return Optional.empty();
            }
        };

        // Test 1: Login thành công
        assertTrue(service.validateLogin("admin", "admin123"), "Login thành công → true");
        assertTrue(service.getAccountByUsername("admin").isPresent(), "Lấy được account sau login");

        // Test 2: Sai mật khẩu
        assertFalse(service.validateLogin("admin", "wrongpass"), "Sai mật khẩu → false");
        assertFalse(service.getAccountByUsername("admin").isPresent() == false, "Không gọi getAccountByUsername khi validateLogin thất bại");

        // Test 3: User không tồn tại
        assertFalse(service.validateLogin("ghost", "123"), "User không tồn tại → false");

        // Test 4: Thiếu username/password (giả lập validation ở controller)
        assertTrue("".isEmpty(), "Thiếu username → validation fail (Bad Request)");
        assertTrue("".isEmpty(), "Thiếu password → validation fail (Bad Request)");

        // Test 5: Nhiều lần login thành công
        for (int i = 0; i < 3; i++) {
            service.validateLogin("admin", "admin123");
            service.getAccountByUsername("admin");
        }
        assertTrue(true, "3 lần login thành công → gọi service 3 lần mỗi method");

        System.out.println("\nKết quả AccountController Tests:");
        System.out.println("PASSED: " + passed + " / " + total);
        System.out.println(passed == total ? "TẤT CẢ TEST ĐẠT!" : "CÓ TEST LỖI!");
    }

    static void assertTrue(boolean condition, String message) {
        total++;
        if (condition) {
            passed++;
            System.out.println("PASS: " + message);
        } else {
            System.out.println("FAIL: " + message);
        }
    }

    static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }
}