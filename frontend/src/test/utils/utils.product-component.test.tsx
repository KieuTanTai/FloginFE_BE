import {render } from "@testing-library/react";
import ProductList from "../../components/products/product-list";
import ProductDetail from "../../components/products/product-detail";
import "@testing-library/jest-dom";

jest.mock("../../services/productService");

describe("ProductList Component Render Test", () => {
    const mockProducts = [
        {
            id: 1,  
            name: "Sách A",
            category: { id: 1, name: "Thể loại 1" },
            quantity: 10,
            price: 100.0,
            imageUrl: "/images/sach-a.jpg",
            deleted: false,
        },
        {
            id: 2,
            name: "Sách B",
            category: { id: 2, name: "Thể loại 2" },
            quantity: 5,
            price: 150.0,
            imageUrl: "/images/sach-b.jpg",
            deleted: true,
        },
    ];

    test("hiển thị danh sách sản phẩm đúng", () => {
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
        expect(getByText("Danh Sách Sách")).toBeInTheDocument();
        expect(getByText("Sách A")).toBeInTheDocument();
        expect(getByText("Sách B")).toBeInTheDocument();
        expect(getAllByText("Sửa").length).toBe(1);
    });

    test("hiển thị trạng thái khi không có sản phẩm", () => {
        const { getByText} = render(
            <ProductList
                products={[]}
                onEdit={jest.fn()}
                onDelete={jest.fn()}
                onRestore={jest.fn()}
                onAdd={jest.fn()}
                onViewDetail={jest.fn()}
                />
        );
        expect(getByText("Chưa có sách nào. Hãy thêm sách mới!")).toBeInTheDocument();
    });
});

describe("ProductDetail Component Render Test", () => {
    const mockProduct = {
        id: 1,
        name: "Sách A",
        description: "Mô tả về Sách A",
        category: { id: 1, name: "Thể loại 1" },
        quantity: 10,
        price: 100.0,
        imageUrl: "/images/sach-a.jpg",
    };
    test("hiển thị chi tiết sản phẩm đúng", () => {
        const { getByText, getByAltText } = render(
            <ProductDetail
                product={mockProduct}
                onClose={jest.fn()}
            />
        );
        expect(getByText("Sách A")).toBeInTheDocument();
        expect(getByText("Mô tả về Sách A")).toBeInTheDocument();
        expect(getByText("Thể loại:")).toBeInTheDocument();
        expect(getByText("Thể loại 1")).toBeInTheDocument();
        expect(getByText("Số lượng:")).toBeInTheDocument();
        expect(getByText("10")).toBeInTheDocument();
        expect(getByText("$100.00")).toBeInTheDocument();
        expect(getByAltText("Sách A")).toBeInTheDocument();
    });

    test("hiển thị chi tiết sản phẩm đúng khi thiếu 1 vài thuộc tính", () => {
        const productWithoutDescriptionAndCategory = {
            id: 2,
            name: "Sách B",
            quantity: 0,
            price: 50.0,    
        };
        const { getByText, queryByText } = render(
            <ProductDetail
                product={productWithoutDescriptionAndCategory}
                onClose={jest.fn()}
            />
        );
        expect(getByText("Sách B")).toBeInTheDocument();
        expect(queryByText("Mô tả về Sách A")).not.toBeInTheDocument();
        expect(queryByText("Thể loại:")).not.toBeInTheDocument();
        expect(getByText("Số lượng:")).toBeInTheDocument();
        expect(getByText("0")).toBeInTheDocument();
        expect(getByText("$50.00")).toBeInTheDocument();
    });
    test("hiển thị chi tiết sản phẩm đúng khi không có thuộc tính", () => {
        const productWithoutOptionalProps = {
            id:3,
            name: "Sách C",
            price: 0.0,
        };
        const { getByText, queryByText } = render(
            <ProductDetail
                product={productWithoutOptionalProps}
                onClose={jest.fn()}
            />
        );
        expect(getByText("Sách C")).toBeInTheDocument();
        expect(queryByText("Mô tả về Sách C")).not.toBeInTheDocument();
        expect(queryByText("Thể loại:")).not.toBeInTheDocument();
        expect(getByText("Số lượng:")).toBeInTheDocument();
        expect(getByText("0")).toBeInTheDocument();
        expect(getByText("$0.00")).toBeInTheDocument();
    });
    
});


                