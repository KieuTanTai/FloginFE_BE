import { validation } from "../../utils/utils";

describe("ProductForm Validation Unit Test", () => {
  //* success: tên hợp lệ
  test("validateProductName - tên hợp lệ", () => {
    const result = validation.validateProductName("Laptop ABC");
    expect(result.valid).toBe(true);
  });

  test("validatePrice - giá hợp lệ", () => {
    const result = validation.validatePrice(999999999);
    expect(result.valid).toBe(true);
  });

  test("validateQuantity - số lượng hợp lệ", () => {
    const result = validation.validateQuantity(99999);
    expect(result.valid).toBe(true);
  });
    
  
  test("validateDescription - mô tả hợp lệ", () => {
    const result = validation.validateDescription("Mô tả hợp lệ");
    expect(result.valid).toBe(true);
  });
    
  test("validateCategory - thể loại", () => {
    const result = validation.validateCategory(1);
    expect(result.valid).toBe(true);
  });
    
  //! Error: tên sản phẩm rỗng
  test("validateProductName - tên sản phẩm rỗng", () => {
    const result = validation.validateProductName("");
    expect(result.valid).toBe(false);
    expect(result.error).toContain("Tên sản phẩm không được để trống");
  });

  //! Error: tên sản phẩm qúa ngắn
  test("validateProductName - tên sản phẩm quá ngắn", () => {
    const result = validation.validateProductName("ab");
    expect(result.valid).toBe(false);
    expect(result.error).toContain("ít nhất 3 ký tự");
  });

  test("validateProductName - tên sản phẩm quá dài", () => {
    const result = validation.validateProductName("a".repeat(101));
    expect(result.valid).toBe(false);
    expect(result.error).toContain("vượt quá 100 ký tự");
  });

  test("validatePrice - giá trị âm", () => {
    const result = validation.validatePrice(-1);
    expect(result.valid).toBe(false);
    expect(result.error).toContain("Giá phải lớn hơn hoặc bằng 0");
  });

  test("validatePrice - giá quá lớn", () => {
    const result = validation.validatePrice(1000000000);
    expect(result.valid).toBe(false);
    expect(result.error).toContain("vượt quá 999,999,999");
  });

  test("validateQuantity - số lượng âm", () => {
    const result = validation.validateQuantity(-1);
    expect(result.valid).toBe(false);
    expect(result.error).toContain("Số lượng phải lớn hơn hoặc bằng 0");
  });

  test("validateQuantity - số lượng quá lớn", () => {
    const result = validation.validateQuantity(100000);
    expect(result.valid).toBe(false);
    expect(result.error).toContain("vượt quá 99,999");
  });

  test("validateDescription - mô tả quá dài", () => {
    const result = validation.validateDescription("a".repeat(501));
    expect(result.valid).toBe(false);
    expect(result.error).toContain("vượt quá 500 ký tự");
  });


  test("validateCategory - chưa chọn thể loại", () => {
    const result = validation.validateCategory(undefined);
    expect(result.valid).toBe(false);
    expect(result.error).toContain("Vui lòng chọn thể loại");
  });


});
