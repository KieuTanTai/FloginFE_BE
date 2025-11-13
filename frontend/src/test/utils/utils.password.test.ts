import { validation } from "../../utils/utils";

describe("Password Validation Tests", () => {
  describe("K1: Password hợp lệ", () => {
    test("TC1: Password hợp lệ với chữ thường và số", () => {
      expect(validation.validatePassword("admin123")).toEqual({ valid: true });
    });

    test("TC2: Password hợp lệ với chữ hoa và số", () => {
      expect(validation.validatePassword("Admin123")).toEqual({ valid: true });
    });

    test("TC3: Password hợp lệ với chữ hoa, thường và số", () => {
      expect(validation.validatePassword("AdminUser123")).toEqual({
        valid: true,
      });
    });

    test("TC4: Password hợp lệ với dấu gạch ngang", () => {
      expect(validation.validatePassword("admin-123")).toEqual({ valid: true });
    });

    test("TC5: Password hợp lệ với dấu chấm", () => {
      expect(validation.validatePassword("admin.123")).toEqual({ valid: true });
    });

    test("TC6: Password hợp lệ với dấu gạch dưới", () => {
      expect(validation.validatePassword("admin_123")).toEqual({ valid: true });
    });

    test("TC7: Password hợp lệ với ký tự đặc biệt @", () => {
      expect(validation.validatePassword("admin@123")).toEqual({ valid: true });
    });

    test("TC8: Password hợp lệ với ký tự đặc biệt #", () => {
      expect(validation.validatePassword("admin#123")).toEqual({ valid: true });
    });

    test("TC9: Password hợp lệ với ký tự đặc biệt $", () => {
      expect(validation.validatePassword("admin$123")).toEqual({ valid: true });
    });

    test("TC10: Password hợp lệ với nhiều ký tự đặc biệt", () => {
      expect(validation.validatePassword("Admin@123#$%")).toEqual({
        valid: true,
      });
    });

    test("TC11: Password hợp lệ với độ dài tối thiểu (6 ký tự)", () => {
      expect(validation.validatePassword("abc123")).toEqual({ valid: true });
    });

    test("TC12: Password hợp lệ với độ dài tối đa (100 ký tự)", () => {
      const password = "a1" + "b".repeat(98);
      expect(validation.validatePassword(password)).toEqual({ valid: true });
    });

    test("TC13: Password hợp lệ với số ở đầu", () => {
      expect(validation.validatePassword("123admin")).toEqual({ valid: true });
    });

    test("TC14: Password hợp lệ với số ở giữa", () => {
      expect(validation.validatePassword("adm123in")).toEqual({ valid: true });
    });

    test("TC15: Password hợp lệ phức tạp", () => {
      expect(validation.validatePassword("P@ssw0rd!2024")).toEqual({
        valid: true,
      });
    });
  });

  describe("K2: Password rỗng hoặc chỉ có khoảng trắng", () => {
    test("TC16: Password rỗng - nên trả về lỗi", () => {
      expect(validation.validatePassword("")).toEqual({
        valid: false,
        error: "Mật khẩu không được để trống",
      });
    });

    test("TC17: Password chỉ có 1 khoảng trắng", () => {
      expect(validation.validatePassword(" ")).toEqual({
        valid: false,
        error: "Mật khẩu không được để trống",
      });
    });

    test("TC18: Password chỉ có nhiều khoảng trắng", () => {
      expect(validation.validatePassword("     ")).toEqual({
        valid: false,
        error: "Mật khẩu không được để trống",
      });
    });

    test("TC19: Password chỉ có tab và khoảng trắng", () => {
      expect(validation.validatePassword("\t  \t  ")).toEqual({
        valid: false,
        error: "Mật khẩu không được để trống",
      });
    });
  });

  describe("K3: Password quá ngắn (< 6 ký tự)", () => {
    test("TC20: Password có 1 ký tự", () => {
      expect(validation.validatePassword("a")).toEqual({
        valid: false,
        error: "Mật khẩu phải có ít nhất 6 ký tự",
      });
    });

    test("TC21: Password có 2 ký tự", () => {
      expect(validation.validatePassword("a1")).toEqual({
        valid: false,
        error: "Mật khẩu phải có ít nhất 6 ký tự",
      });
    });

    test("TC22: Password có 3 ký tự", () => {
      expect(validation.validatePassword("ab1")).toEqual({
        valid: false,
        error: "Mật khẩu phải có ít nhất 6 ký tự",
      });
    });

    test("TC23: Password có 4 ký tự", () => {
      expect(validation.validatePassword("abc1")).toEqual({
        valid: false,
        error: "Mật khẩu phải có ít nhất 6 ký tự",
      });
    });

    test("TC24: Password có 5 ký tự", () => {
      expect(validation.validatePassword("abc12")).toEqual({
        valid: false,
        error: "Mật khẩu phải có ít nhất 6 ký tự",
      });
    });
  });

  describe("K4: Password quá dài (> 100 ký tự)", () => {
    test("TC25: Password có 101 ký tự", () => {
      const password = "a1" + "b".repeat(99);
      expect(validation.validatePassword(password)).toEqual({
        valid: false,
        error: "Mật khẩu không được vượt quá 100 ký tự",
      });
    });
  });

  describe("K5: Password thiếu chữ cái hoặc số", () => {
    test("TC29: Password chỉ có chữ cái (6 ký tự)", () => {
      expect(validation.validatePassword("abcdef")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC30: Password chỉ có chữ cái (10 ký tự)", () => {
      expect(validation.validatePassword("abcdefghij")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC31: Password chỉ có chữ cái và ký tự đặc biệt", () => {
      expect(validation.validatePassword("abc@def#")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC32: Password chỉ có số (6 ký tự)", () => {
      expect(validation.validatePassword("123456")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC33: Password chỉ có số (10 ký tự)", () => {
      expect(validation.validatePassword("1234567890")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC34: Password chỉ có số và ký tự đặc biệt", () => {
      expect(validation.validatePassword("123@456#")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC35: Password chỉ có ký tự đặc biệt", () => {
      expect(validation.validatePassword("@#$%^&")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC36: Password chỉ có khoảng trắng và ký tự đặc biệt", () => {
      expect(validation.validatePassword("  @#$%  ")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC37: Password có chữ cái nhưng thiếu số (nhiều ký tự đặc biệt)", () => {
      expect(validation.validatePassword("Admin@#$%^&*")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });

    test("TC38: Password có số nhưng thiếu chữ cái (nhiều ký tự đặc biệt)", () => {
      expect(validation.validatePassword("123@#$%^&*")).toEqual({
        valid: false,
        error: "Mật khẩu phải chứa cả chữ cái và số",
      });
    });
  });
});
