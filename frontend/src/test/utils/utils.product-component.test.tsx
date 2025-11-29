import {findByText, getByLabelText, render } from "@testing-library/react";
import ProductList from "../../components/products/product-list";
import ProductDetail from "../../components/products/product-detail";
import "@testing-library/jest-dom";
import ProductForm from "../../components/products/product-form";
import api from '../../services/api'

jest.mock("../../services/api");

describe("ProductList Component Integration Test",() => {

    const mockProducts = [
    {
        id: 1,
        name: "Laptop A",
        imageUrl: "/images/laptop-a.jpg",
        price: 100.0,
        description:"des 1",
        category: { id: 1, name: "Loại 1" , description:"des 1"},
        quantity: 10,
        deleted: false,
    },
    {
        id: 2,
        name: "Laptop B",
        imageUrl: "/images/laptop-b.jpg",
        price: 150.0, 
        description:"des 2",
        category: { id: 2, name: "Loại 2", description:"des 2" },
        quantity: 5,         
        deleted: true,
    },
];
    test("hiển thị danh sách sản phẩm đúng",async () => {

        (api.get as jest.Mock).mockResolvedValue({
            data: mockProducts,
        });
        const {findByText, getAllByText } = render(
            <ProductList
                products={mockProducts}
                onEdit={jest.fn()}
                onDelete={jest.fn()}
                onRestore={jest.fn()}
                onAdd={jest.fn()}
                onViewDetail={jest.fn()}
            />
        );
        expect(await findByText("Danh Sách Laptop")).toBeInTheDocument();
        
        expect(await findByText("Laptop A")).toBeInTheDocument();
        expect(await findByText("Thể loại: Loại 1")).toBeInTheDocument();
        expect(await findByText('Số lượng: 10')).toBeInTheDocument();
        expect(await findByText('$100.00')).toBeInTheDocument();

        expect(await findByText("Laptop B")).toBeInTheDocument();
        expect(await findByText("Thể loại: Loại 2")).toBeInTheDocument();
        expect(await findByText('Số lượng: 5')).toBeInTheDocument();
        expect(await findByText('$150.00')).toBeInTheDocument();
        
        expect(getAllByText("Sửa").length).toBe(1);
        expect(getAllByText("Xóa").length).toBe(1);
        expect(getAllByText("Mở Khóa").length).toBe(1);
    });

    test("hiển thị trạng thái khi không có sản phẩm", async () => {
        const { findByText} = render(
            <ProductList
                products={[]}
                onEdit={jest.fn()}
                onDelete={jest.fn()}
                onRestore={jest.fn()}
                onAdd={jest.fn()}
                onViewDetail={jest.fn()}
                />
        );
        expect(await findByText("Chưa có laptop nào. Hãy thêm laptop mới!")).toBeInTheDocument();
    });
});

describe("ProductForm Component Render Test", () =>{
    
    const mockProduct = {
        id: 1,
        name: "Laptop A",
        imageUrl: "/images/laptop-a.jpg",
        price: 100.0,
        description:"des 1",
        category: { id: 1, name: "Loại 1" , description:"des 1"},
        quantity: 10,
        deleted: false,
    }

    test("Hiển thị Form chỉnh sửa sách", async () => {
        (api.put as jest.Mock).mockResolvedValue({
            data: mockProduct,
        });
        const {getByLabelText, findByText, getByAltText } = render(
            <ProductForm 
                product={mockProduct}
                onSubmit={jest.fn()}
                onCancel={jest.fn()}
            />
        );

        expect(await findByText("Chỉnh Sửa Laptop")).toBeInTheDocument();
        
        expect(await findByText("Tên Laptop *")).toBeInTheDocument();
        expect(await getByLabelText("Tên Laptop *")).toHaveValue("Laptop A");

        expect(await findByText("Hình Ảnh Laptop")).toBeInTheDocument();
        const img = getByAltText("Preview");
        expect(img).toBeInTheDocument();
        expect(img).toHaveAttribute("src", "/images/laptop-a.jpg");

        expect(await findByText("Loại Laptop *")).toBeInTheDocument();
        expect(await getByLabelText("Loại Laptop *")).toHaveValue("1");

        expect(await findByText("Giá ($) *")).toBeInTheDocument();
        expect(await getByLabelText("Giá ($) *")).toHaveValue(100.00);
        
        expect(await findByText("Số Lượng *")).toBeInTheDocument();
        expect(await getByLabelText("Số Lượng *")).toHaveValue(10);
        
        expect(await findByText("Cập Nhật")).toBeInTheDocument();
        expect(await findByText("Hủy")).toBeInTheDocument();
    });

    test("Hiển thị Form thêm sách", async () => {
        const {getByLabelText, findByText } = render(
            <ProductForm 
                product={null}
                onSubmit={jest.fn()}
                onCancel={jest.fn()}
            />
        );

        expect(await findByText("Thêm Laptop Mới")).toBeInTheDocument();
        
        expect(await findByText("Tên Laptop *")).toBeInTheDocument();
        expect(await getByLabelText("Tên Laptop *")).toHaveValue("");

        expect(await findByText("Hình Ảnh Laptop")).toBeInTheDocument();

        expect(await findByText("Loại Laptop *")).toBeInTheDocument();
        expect(await getByLabelText("Loại Laptop *")).toHaveValue("");

        expect(await findByText("Giá ($) *")).toBeInTheDocument();
        expect(await getByLabelText("Giá ($) *")).toHaveValue(0);
        
        expect(await findByText("Số Lượng *")).toBeInTheDocument();
        expect(await getByLabelText("Số Lượng *")).toHaveValue(10);
        
        expect(await findByText("Thêm Laptop")).toBeInTheDocument();
        expect(await findByText("Hủy")).toBeInTheDocument();
    });
})

describe("ProductDetail Component Render Test", () => {
    const mockProduct = {
        id: 1,
        name: "Laptop A",
        imageUrl: "/images/laptop-a.jpg",
        price: 100.0,
        description:"des 1",
        category: { id: 1, name: "Loại 1" , description:"des 1"},
        quantity: 10,
        deleted: false,
    };
    test("hiển thị chi tiết sản phẩm đúng", async () => {
        const { findByText, getByAltText } = render(
            <ProductDetail
                product={mockProduct}
                onClose={jest.fn()}
            />
        );

        const img = getByAltText("Laptop A");
        expect(img).toBeInTheDocument();
        expect(img).toHaveAttribute("src", "/images/laptop-a.jpg");
        
        expect(await findByText("Laptop A")).toBeInTheDocument();
        
        expect(await findByText("des 1")).toBeInTheDocument();

        expect(await findByText("Loại laptop:")).toBeInTheDocument();
        expect(await findByText("Loại 1")).toBeInTheDocument();
        
        expect(await findByText("Số lượng:")).toBeInTheDocument();
        expect(await findByText("10")).toBeInTheDocument();
        
        expect(await findByText("Giá:")).toBeInTheDocument();
        expect(await findByText("$100.00")).toBeInTheDocument();
        
        expect(await findByText("Đóng")).toBeInTheDocument();
    });

    test("hiển thị chi tiết sản phẩm đúng khi thiếu 1 vài thuộc tính", async () => {
        const productWithoutDescriptionAndCategory = {
            id: 2,
            name: "Laptop A",
            imageUrl: "",
            price: 0,
            description:"des 1",
            category: { id: 1, name: "Loại 1" , description:"des 1"},
            quantity: 0,
            deleted: false,
        };
        const { findByText } = render(
            <ProductDetail
                product={productWithoutDescriptionAndCategory}
                onClose={jest.fn()}
            />
        );
        expect(await findByText("Laptop A")).toBeInTheDocument();
        
        expect(await findByText("des 1")).toBeInTheDocument();

        expect(await findByText("Loại laptop:")).toBeInTheDocument();
        expect(await findByText("Loại 1")).toBeInTheDocument();

        expect(await findByText("Số lượng:")).toBeInTheDocument();
        expect(await findByText("0")).toBeInTheDocument();
        
        expect(await findByText("Giá:")).toBeInTheDocument();
        expect(await findByText("$0.00")).toBeInTheDocument();
        
        expect(await findByText("Đóng")).toBeInTheDocument();
    });
    test("hiển thị chi tiết sản phẩm đúng khi không có thuộc tính", async () => {
        const productWithoutOptionalProps = {
            id: 3,
            name: "",
            imageUrl: "",
            price: 0,
            description:"",
            category: { id: 1, name: "" , description:""},
            quantity: 0,
            deleted: false,
        };
        const { findByText} = render(
            <ProductDetail
                product={productWithoutOptionalProps}
                onClose={jest.fn()}
            />
        );


        expect(await findByText("Loại laptop:")).toBeInTheDocument();

        expect(await findByText("Số lượng:")).toBeInTheDocument();
        expect(await findByText("0")).toBeInTheDocument();
        
        expect(await findByText("Giá:")).toBeInTheDocument();
        expect(await findByText("$0.00")).toBeInTheDocument();
        
    });
    
});


                