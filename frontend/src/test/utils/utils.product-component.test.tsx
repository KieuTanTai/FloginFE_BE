import { render } from "@testing-library/react";
import ProductList from "../../components/products/product-list";
import ProductDetail from "../../components/products/product-detail";
import "@testing-library/jest-dom";

jest.mock("../../services/api");

describe("ProductList Component Integration Test", () => {

    //! K1: ProductList Component Render Test

    describe('K1: ProductList Component Render Test', () => {
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

        //! TC1: Hiển thị danh sách laptop đúng
        test('TC1: Hiển thị danh sách laptop đúng', () => {
            const { getByText, getAllByText } = render(
                <ProductList
                    products={mockProducts}
                    onEdit={jest.fn()}
                    onDelete={jest.fn()}
                    onRestore={jest.fn()}
                    onAdd={jest.fn()}
                    onViewDetail={jest.fn()}
                />
            );
            expect(getByText("Danh Sách Laptop")).toBeInTheDocument();
            expect(getByText("Laptop Dell XPS 13")).toBeInTheDocument();
            expect(getByText("Laptop MacBook Pro 16")).toBeInTheDocument();
            expect(getAllByText("Sửa").length).toBe(2);
        });

        //! TC2: Hiển thị trạng thái khi không có laptop
        test('TC2: Hiển thị trạng thái khi không có laptop', () => {
            const { getByText } = render(
                <ProductList
                    products={[]}
                    onEdit={jest.fn()}
                    onDelete={jest.fn()}
                    onRestore={jest.fn()}
                    onAdd={jest.fn()}
                    onViewDetail={jest.fn()}
                />
            );
            expect(getByText("Chưa có laptop nào. Hãy thêm laptop mới!")).toBeInTheDocument();
        });
    });

    //! K2: ProductDetail Component Render Test

    describe('K2: ProductDetail Component Render Test', () => {
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

        //! TC1: Hiển thị chi tiết laptop đầy đủ
        test('TC1: Hiển thị chi tiết laptop đầy đủ', () => {
            const { getByText, getByAltText } = render(
                <ProductDetail
                    product={mockProduct}
                    onClose={jest.fn()}
                />
            );
            expect(getByText("Laptop Dell XPS 13")).toBeInTheDocument();
            expect(getByText("Laptop mỏng nhẹ, hiệu năng cao.")).toBeInTheDocument();
            expect(getByText("Thể loại:")).toBeInTheDocument();
            expect(getByText("Laptop")).toBeInTheDocument();
            expect(getByText("Số lượng:")).toBeInTheDocument();
            expect(getByText("5")).toBeInTheDocument();
            expect(getByText("25000000")).toBeInTheDocument();
            expect(getByAltText("Laptop Dell XPS 13")).toBeInTheDocument();
        });

        //! TC2: Hiển thị chi tiết laptop khi thiếu một vài thuộc tính
        test('TC2: Hiển thị chi tiết laptop khi thiếu một vài thuộc tính', () => {
            const productWithoutDescriptionAndCategory = {
                id: 2,
                name: "Laptop MacBook Pro 16",
                imageUrl: "/images/macbook-pro-16.jpg",
                price: 60000000,
                quantity: 3,
                deleted: false,
            };
            const { queryByText, getByText } = render(
                <ProductDetail
                    product={productWithoutDescriptionAndCategory}
                    onClose={jest.fn()}
                />
            );
            expect(getByText("Laptop MacBook Pro 16")).toBeInTheDocument();
            expect(queryByText("Thể loại:")).not.toBeInTheDocument();
            expect(queryByText("Mô tả về sản phẩm")).not.toBeInTheDocument();
            expect(getByText("Số lượng:")).toBeInTheDocument();
            expect(getByText("3")).toBeInTheDocument();
            expect(getByText("60000000")).toBeInTheDocument();
        });

        //! TC3: Hiển thị chi tiết laptop khi không có thuộc tính tuỳ chọn
        test('TC3: Hiển thị chi tiết laptop khi không có thuộc tính tuỳ chọn', () => {
            const productWithoutOptionalProps = {
                id: 3,
                name: "Laptop HP Pavilion 15",
                price: 18000000,
                deleted: false,
            };
            const { queryByText, getByText } = render(
                <ProductDetail
                    product={productWithoutOptionalProps}
                    onClose={jest.fn()}
                />
            );
            expect(getByText("Laptop HP Pavilion 15")).toBeInTheDocument();
            expect(queryByText("Thể loại:")).not.toBeInTheDocument();
            expect(queryByText("Mô tả về sản phẩm")).not.toBeInTheDocument();
            expect(getByText("Số lượng:")).toBeInTheDocument();
            expect(getByText("0")).toBeInTheDocument();
            expect(getByText("18000000")).toBeInTheDocument();
        });
    });
});


