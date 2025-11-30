import { render, screen } from "@testing-library/react";
import ProductList from "../../components/products/product-list";
import ProductDetail from "../../components/products/product-detail";
import ProductForm from "../../components/products/product-form";
import "@testing-library/jest-dom";
import api from "../../services/api";

jest.mock("../../services/api");

// ---------------------------------------------------------
// ProductList Integration Test
// ---------------------------------------------------------
describe("ProductList Component Integration Test", () => {
    const mockProducts = [
        {
            id: 1,
            name: "Laptop A",
            imageUrl: "/images/laptop-a.jpg",
            price: 100.0,
            description: "des 1",
            category: { id: 1, name: "Loại 1", description: "des 1" },
            quantity: 10,
            deleted: false,
        },
        {
            id: 2,
            name: "Laptop B",
            imageUrl: "/images/laptop-b.jpg",
            price: 150.0,
            description: "des 2",
            category: { id: 2, name: "Loại 2", description: "des 2" },
            quantity: 5,
            deleted: true,
        },
    ];

    test("hiển thị danh sách sản phẩm đúng", async () => {
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

        expect(await screen.findByText("Danh Sách Laptop")).toBeInTheDocument();

        // Laptop A
        expect(await screen.findByText("Laptop A")).toBeInTheDocument();
        expect(await screen.findByText("Thể loại: Loại 1")).toBeInTheDocument();
        expect(await screen.findByText("Số lượng: 10")).toBeInTheDocument();
        expect(await screen.findByText("$100.00")).toBeInTheDocument();

        // Laptop B
        expect(await screen.findByText("Laptop B")).toBeInTheDocument();
        expect(await screen.findByText("Thể loại: Loại 2")).toBeInTheDocument();
        expect(await screen.findByText("Số lượng: 5")).toBeInTheDocument();
        expect(await screen.findByText("$150.00")).toBeInTheDocument();

        // Nút hành động
        expect(screen.getAllByText("Sửa")).toHaveLength(1);
        expect(screen.getAllByText("Xóa")).toHaveLength(1);
        expect(screen.getAllByText("Mở Khóa")).toHaveLength(1);
    });

    test("hiển thị trạng thái khi không có sản phẩm", async () => {
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
            await screen.findByText("Chưa có laptop nào. Hãy thêm laptop mới!")
        ).toBeInTheDocument();
    });
});

// ---------------------------------------------------------
// ProductForm Component Render Test
// ---------------------------------------------------------
describe("ProductForm Component Render Test", () => {
    const mockProduct = {
        id: 1,
        name: "Laptop A",
        imageUrl: "/images/laptop-a.jpg",
        price: 100.0,
        description: "des 1",
        category: { id: 1, name: "Loại 1", description: "des 1" },
        quantity: 10,
        deleted: false,
    };

    test("Hiển thị Form chỉnh sửa laptop", async () => {
        render(
            <ProductForm
                product={mockProduct}
                onSubmit={jest.fn()}
                onCancel={jest.fn()}
            />
        );

        expect(await screen.findByText("Chỉnh Sửa Laptop")).toBeInTheDocument();

        // name
        expect(screen.getByLabelText("Tên Laptop *")).toHaveValue("Laptop A");

        // image preview
        const img = screen.getByAltText("Preview");
        expect(img).toHaveAttribute("src", "/images/laptop-a.jpg");

        // category
        expect(screen.getByLabelText("Loại Laptop *")).toHaveValue("1");

        // price
        expect(screen.getByLabelText("Giá ($) *")).toHaveValue(100);

        // quantity
        expect(screen.getByLabelText("Số Lượng *")).toHaveValue(10);

        expect(screen.getByText("Cập Nhật")).toBeInTheDocument();
        expect(screen.getByText("Hủy")).toBeInTheDocument();
    });

    test("Hiển thị Form thêm laptop", async () => {
        render(
            <ProductForm
                product={null}
                onSubmit={jest.fn()}
                onCancel={jest.fn()}
            />
        );

        expect(await screen.findByText("Thêm Laptop Mới")).toBeInTheDocument();

        expect(screen.getByLabelText("Tên Laptop *")).toHaveValue("");
        expect(screen.getByLabelText("Loại Laptop *")).toHaveValue("");
        expect(screen.getByLabelText("Giá ($) *")).toHaveValue(0);
        expect(screen.getByLabelText("Số Lượng *")).toHaveValue(10); // default form value

        expect(screen.getByText("Thêm Laptop")).toBeInTheDocument();
        expect(screen.getByText("Hủy")).toBeInTheDocument();
    });
});

// ---------------------------------------------------------
// ProductDetail Component Render Test
// ---------------------------------------------------------
describe("ProductDetail Component Render Test", () => {
    const mockProduct = {
        id: 1,
        name: "Laptop A",
        imageUrl: "/images/laptop-a.jpg",
        price: 100.0,
        description: "des 1",
        category: { id: 1, name: "Loại 1", description: "des 1" },
        quantity: 10,
        deleted: false,
    };

    test("hiển thị chi tiết sản phẩm đầy đủ", async () => {
        render(<ProductDetail product={mockProduct} onClose={jest.fn()} />);

        const img = screen.getByAltText("Laptop A");
        expect(img).toHaveAttribute("src", "/images/laptop-a.jpg");

        expect(await screen.findByText("Laptop A")).toBeInTheDocument();
        expect(await screen.findByText("des 1")).toBeInTheDocument();
        expect(await screen.findByText("Loại 1")).toBeInTheDocument();
        expect(await screen.findByText("10")).toBeInTheDocument();
        expect(await screen.findByText("$100.00")).toBeInTheDocument();
    });

    test("hiển thị khi thiếu một số thuộc tính", async () => {
        const p = {
            id: 2,
            name: "Laptop A",
            imageUrl: "",
            price: 0,
            description: "des 1",
            category: { id: 1, name: "Loại 1", description: "des 1" },
            quantity: 0,
            deleted: false,
        };

        render(<ProductDetail product={p} onClose={jest.fn()} />);

        expect(await screen.findByText("Laptop A")).toBeInTheDocument();
        expect(await screen.findByText("des 1")).toBeInTheDocument();
        expect(await screen.findByText("Loại 1")).toBeInTheDocument();
        expect(await screen.findByText("0")).toBeInTheDocument();
        expect(await screen.findByText("$0.00")).toBeInTheDocument();
    });

    test("hiển thị khi không có thuộc tính optional", async () => {
        const p = {
            id: 3,
            name: "",
            imageUrl: "",
            price: 0,
            description: "",
            category: { id: 1, name: "", description: "" },
            quantity: 0,
            deleted: false,
        };

        render(<ProductDetail product={p} onClose={jest.fn()} />);

        expect(await screen.findByText("Số lượng:")).toBeInTheDocument();
        expect(await screen.findByText("0")).toBeInTheDocument();
        expect(await screen.findByText("$0.00")).toBeInTheDocument();
    });
});
