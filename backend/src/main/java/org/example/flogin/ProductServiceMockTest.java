import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class CategoryDTO {
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

class ProductDTO {
    private String name;
    private BigDecimal price;
    private int quantity;
    private CategoryDTO category;
    private Long id;
    private boolean deleted;

    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal price) { this.price = price; }
    public int getQuantity() { return quantity; } public void setQuantity(int quantity) { this.quantity = quantity; }
    public CategoryDTO getCategory() { return category; } public void setCategory(CategoryDTO c) { this.category = c; }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public boolean getDeleted() { return deleted; } public void setDeleted(boolean d) { this.deleted = d; }
}

class Category {
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private Category category;
    private boolean deleted = false;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String n) { this.name = n; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal p) { this.price = p; }
    public int getQuantity() { return quantity; } public void setQuantity(int q) { this.quantity = q; }
    public Category getCategory() { return category; } public void setCategory(Category c) { this.category = c; }
    public boolean getDeleted() { return deleted; } public void setDeleted(boolean d) { this.deleted = d; }
}

interface ProductRepository {
    Product save(Product p);
    Optional<Product> findById(Long id);
    Optional<Product> findByIdAndDeletedFalse(Long id);
    boolean existsById(Long id);
}

interface CategoryRepository {
    boolean existsById(Long id);
    Optional<Category> findById(Long id);
}

class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductService(ProductRepository pr, CategoryRepository cr) {
        this.productRepo = pr;
        this.categoryRepo = cr;
    }

    public ProductDTO createProduct(ProductDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty())
            throw new IllegalArgumentException("Tên không được trống");
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Giá phải > 0");
        if (!categoryRepo.existsById(dto.getCategory().getId()))
            throw new IllegalArgumentException("Category không tồn tại");

        Product p = new Product();
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setQuantity(dto.getQuantity());
        Product saved = productRepo.save(p);
        dto.setId(saved.getId());
        return dto;
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepo.findByIdAndDeletedFalse(id).map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setDeleted(p.getDeleted());
            return dto;
        });
    }

    public void softDeleteProduct(Long id) {
        Product p = productRepo.findById(id).orElseThrow();
        p.setDeleted(true);
        productRepo.save(p);
    }
}

public class ProductServiceMockTest {
    static int passed = 0, total = 0;

    public static void main(String[] args) {
        System.out.println("4.2 ProductService Mock Testing - Giả lập trên JDoodle\n");

        ProductRepository productRepo = new ProductRepository() {
            Map<Long, Product> db = new HashMap<>();
            long nextId = 1L;

            { // Khởi tạo dữ liệu giả
                Category cat = new Category(); cat.setId(1L);
                Product p = new Product();
                p.setId(nextId++);
                p.setName("ASUS ROG");
                p.setPrice(new BigDecimal("1500"));
                p.setQuantity(10);
                p.setCategory(cat);
                db.put(1L, p);
            }

            @Override public Product save(Product p) {
                if (p.getId() == null) p.setId(nextId++);
                db.put(p.getId(), p);
                System.out.println("   [Mock Repo] save() → id = " + p.getId());
                return p;
            }
            @Override public Optional<Product> findById(Long id) { return Optional.ofNullable(db.get(id)); }
            @Override public Optional<Product> findByIdAndDeletedFalse(Long id) {
                Product p = db.get(id);
                return (p != null && !p.getDeleted()) ? Optional.of(p) : Optional.empty();
            }
            @Override public boolean existsById(Long id) { return db.containsKey(id); }
        };

        CategoryRepository categoryRepo = new CategoryRepository() {
            @Override public boolean existsById(Long id) {
                boolean exists = id == 1L;
                System.out.println("   [Mock CategoryRepo] existsById(" + id + ") → " + exists);
                return exists;
            }
            @Override public Optional<Category> findById(Long id) {
                if (id == 1L) {
                    Category c = new Category(); c.setId(1L); return Optional.of(c);
                }
                return Optional.empty();
            }
        };

        ProductService service = new ProductService(productRepo, categoryRepo);

        // Test create thành công
        ProductDTO dto = new ProductDTO();
        dto.setName("MSI Titan");
        dto.setPrice(new BigDecimal("2000"));
        dto.setQuantity(5);
        CategoryDTO catDto = new CategoryDTO();
        catDto.setId(1L);
        dto.setCategory(catDto);

        try {
            service.createProduct(dto);
            assertTrue(true, "Create product thành công");
        } catch (Exception e) {
            fail("Không được throw khi category tồn tại");
        }

        // Test create thất bại - category không tồn tại
        catDto.setId(999L);
        try {
            service.createProduct(dto);
            fail("Phải throw khi category không tồn tại");
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Throw đúng khi category không tồn tại");
        }

        // Test soft delete
        service.softDeleteProduct(1L);
        assertTrue(service.getProductById(1L).isEmpty(), "Soft delete → không tìm thấy bằng findByIdAndDeletedFalse");

        System.out.println("\nKết quả ProductService Tests:");
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

    static void fail(String message) {
        total++;
        System.out.println("FAIL: " + message);
    }
}