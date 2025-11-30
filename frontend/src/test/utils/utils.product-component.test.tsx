import { render, screen } from "@testing-library/react";
import ProductList from "../../components/products/product-list";
import ProductDetail from "../../components/products/product-detail";
import "@testing-library/jest-dom";

describe("ProductList Component – Integration Render Tests", () => {
    const mockProducts = [
        {
            id: 1,
            name: "Laptop Dell XPS 13",
            imageUrl: "/images/laptop-dell-xps13.jpg",
            price: 25000000,
            description: "Laptop mỏng nhẹ, hiệu năng cao.",
            category: { id: 1, name: "Laptop" },
            quantity: 5,
            deleted: false,
        },
        {
            id: 2,
            name: "Laptop MacBook Pro 16",
            imageUrl: "/images/macbook-pro-16.jpg",
            price: 60000000,
            description: "Laptop hiệu năng mạnh mẽ cho công việc chuyên nghiệp.",
            category: { id: 1, name: "Laptop" },
            quantity: 3,
            deleted: false,
        },
    ];

    //! TC1 – render danh sách laptop đầy đủ
    test("TC1: Hiển thị danh sách laptop đúng", () => {
        render(
            <ProductList
                products={mockProducts}
                onEdit={jest.fn()}
                onDelete={jest.fn()}
                onRestore={jest.fn()}
                onAdd={jest.fn()}
                onViewDetail={jest.fn()}
            />
        );

        expect(screen.getByText("Danh Sách Laptop")).toBeInTheDocument();
        expect(screen.getByText("Laptop Dell XPS 13")).toBeInTheDocument();
        expect(screen.getByText("Laptop MacBook Pro 16")).toBeInTheDocument();

        // có 2 nút sửa
        expect(screen.getAllByText("Sửa")).toHaveLength(2);
    });

    //! TC2 – không có sản phẩm
    test("TC2: Hiển thị trạng thái khi không có laptop", () => {
        render(
            <ProductList
                products={[]}
                onEdit={jest.fn()}
                onDelete={jest.fn()}
                onRestore={jest.fn()}
                onAdd={jest.fn()}
                onViewDetail={jest.fn()}
            />
        );

        expect(
            screen.getByText("Chưa có laptop nào. Hãy thêm laptop mới!")
        ).toBeInTheDocument();
    });
});


// ----------------------------------------------------------
// ProductDetail Tests
// ----------------------------------------------------------

describe("ProductDetail Component – Integration Render Tests", () => {
    const mockProduct = {
        id: 1,
        name: "Laptop Dell XPS 13",
        imageUrl: "/images/laptop-dell-xps13.jpg",
        price: 25000000,
        description: "Laptop mỏng nhẹ, hiệu năng cao.",
        category: { id: 1, name: "Laptop" },
        quantity: 5,
        deleted: false,
    };

    //! TC1 – render đầy đủ thông tin
    test("TC1: Hiển thị chi tiết laptop đầy đủ", () => {
        render(<ProductDetail product={mockProduct} onClose={jest.fn()} />);

        expect(screen.getByText("Laptop Dell XPS 13")).toBeInTheDocument();
        expect(screen.getByText("Laptop mỏng nhẹ, hiệu năng cao.")).toBeInTheDocument();
        expect(screen.getByText("Loại laptop:")).toBeInTheDocument();
        expect(screen.getByText("Laptop")).toBeInTheDocument();
        expect(screen.getByText("Số lượng:")).toBeInTheDocument();
        expect(screen.getByText("5")).toBeInTheDocument();
        expect(screen.getByText("$25000000.00")).toBeInTheDocument();

        expect(screen.getByAltText("Laptop Dell XPS 13")).toBeInTheDocument();
    });

    //! TC2 – thiếu description & category
    test("TC2: Hiển thị chi tiết khi thiếu description và category", () => {
        const productWithoutDescriptionAndCategory = {
            id: 2,
            name: "Laptop MacBook Pro 16",
            imageUrl: "/images/macbook-pro-16.jpg",
            price: 60000000,
            quantity: 3,
            deleted: false,
        };

        render(
            <ProductDetail
                product={productWithoutDescriptionAndCategory}
                onClose={jest.fn()}
            />
        );

        expect(screen.getByText("Laptop MacBook Pro 16")).toBeInTheDocument();
        expect(screen.queryByText("Thể loại:")).not.toBeInTheDocument();
        expect(screen.queryByText("Mô tả về sản phẩm")).not.toBeInTheDocument();

        expect(screen.getByText("Số lượng:")).toBeInTheDocument();
        expect(screen.getByText("3")).toBeInTheDocument();
        expect(screen.getByText("$60000000.00")).toBeInTheDocument();
    });

    //! TC3 – thiếu toàn bộ optional props
    test("TC3: Hiển thị chi tiết khi thiếu toàn bộ thuộc tính tùy chọn", () => {
        const productWithoutOptionalProps = {
            id: 3,
            name: "Laptop HP Pavilion 15",
            price: 0,
            deleted: false,
        };

        render(
            <ProductDetail
                product={productWithoutOptionalProps}
                onClose={jest.fn()}
            />
        );

        expect(screen.getByText("Laptop HP Pavilion 15")).toBeInTheDocument();

        expect(screen.queryByText("Thể loại:")).not.toBeInTheDocument();
        expect(screen.queryByText("Mô tả về sản phẩm")).not.toBeInTheDocument();

        // default quantity = 0
        expect(screen.getByText("Số lượng:")).toBeInTheDocument();
        expect(screen.getByText("0")).toBeInTheDocument();

        expect(screen.getByText("$0.00")).toBeInTheDocument();
    });
});
