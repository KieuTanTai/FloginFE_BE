
import "@testing-library/jest-dom";
import { productService } from "../../services/productService";

jest.mock("../../services/productService");
const mockedService = productService as jest.Mocked<typeof productService>;

describe("Product Frontend Mock Test",() =>{
    afterEach(() => {
        jest.clearAllMocks();
    });
    describe("Mock CRUD operations",()=>{
        describe('Mock Create operation', () => {
            test("Success case",async ()=>{
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

                mockedService.createProduct.mockResolvedValue(mockProduct);

                const result = await mockedService.createProduct(mockProduct);

                expect(result).toEqual(mockProduct);
                expect(mockedService.createProduct).toHaveBeenCalledTimes(1);
                expect(mockedService.createProduct).toHaveBeenCalledWith(mockProduct);

            });

            test("Failure case | Thất bại trong việc thêm ",async ()=>{
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

                mockedService.createProduct.mockRejectedValue(new Error('Failed to create product. Please try again.'));

                await expect(
                    mockedService.createProduct(mockProduct)
                ).rejects.toThrow("Failed to create product. Please try again.");

                expect(mockedService.createProduct).toHaveBeenCalledTimes(1);
                expect(mockedService.createProduct).toHaveBeenCalledWith(mockProduct);

            });
        });

        describe("Mock Read operation",() =>{
            test("Success case",async ()=>{
                const mockProducts = [
                    {
                        id: 1,
                        name: "Laptop Dell XPS 13",
                        imageUrl: "/images/laptop-dell-xps13.jpg",
                        price: 25000000,
                        description: "Laptop mỏng nhẹ, hiệu năng cao.",
                        category: { id: 1, name: "Laptop", description:"des" },
                        quantity: 5,
                        deleted: false,
                    },
                    {
                        id: 2,
                        name: "Laptop MacBook Pro 16",
                        imageUrl: "/images/macbook-pro-16.jpg",
                        price: 60000000,
                        description: "Laptop hiệu năng mạnh mẽ cho công việc chuyên nghiệp.",
                        category: { id: 1, name: "Laptop", description:"des" },
                        quantity: 3,
                        deleted: false,
                    },
                ];

                mockedService.getAllProducts.mockResolvedValue(mockProducts);

                const result = await mockedService.getAllProducts();

                expect(result).toEqual(mockProducts);
                expect(mockedService.getAllProducts).toHaveBeenCalledTimes(1);
                expect(mockedService.getAllProducts).toHaveBeenCalledWith();

            });

            test("Failure case | Thất bại trong việc lấy dữ liệu",async () => {
                const mockProducts = [
                    {
                        id: 1,
                        name: "Laptop Dell XPS 13",
                        imageUrl: "/images/laptop-dell-xps13.jpg",
                        price: 25000000,
                        description: "Laptop mỏng nhẹ, hiệu năng cao.",
                        category: { id: 1, name: "Laptop", description:"des" },
                        quantity: 5,
                        deleted: false,
                    },
                    {
                        id: 2,
                        name: "Laptop MacBook Pro 16",
                        imageUrl: "/images/macbook-pro-16.jpg",
                        price: 60000000,
                        description: "Laptop hiệu năng mạnh mẽ cho công việc chuyên nghiệp.",
                        category: { id: 1, name: "Laptop", description:"des" },
                        quantity: 3,
                        deleted: false,
                    },
                ];

                mockedService.getAllProducts.mockRejectedValueOnce(null);

                await expect(mockedService.getAllProducts()).rejects.toBe(null);
                expect(mockedService.getAllProducts).toHaveBeenCalledTimes(1);
                expect(mockedService.getAllProducts).toHaveBeenCalledWith();
            });
        });

        describe("",() =>{
            test("Success case",async ()=>{
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

                mockedService.updateProduct.mockResolvedValue(mockProduct);

                const result = await mockedService.updateProduct(mockProduct.id,mockProduct);

                expect(result).toEqual(mockProduct);
                expect(mockedService.updateProduct).toHaveBeenCalledTimes(1);
                expect(mockedService.updateProduct).toHaveBeenCalledWith(mockProduct.id,mockProduct);

            });

            test("Failure case | Cập nhật không thành công",async ()=>{
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

                mockedService.updateProduct.mockRejectedValue(new Error('Failed to update product. Please try again.'));

                await expect(mockedService.updateProduct(mockProduct.id,mockProduct)).rejects.toThrow('Failed to update product. Please try again.');
                expect(mockedService.updateProduct).toHaveBeenCalledTimes(1);
                expect(mockedService.updateProduct).toHaveBeenCalledWith(mockProduct.id,mockProduct);

            });
        });

        describe("Mock Delete operation",() =>{

            test("Success case",async ()=>{
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

                mockedService.softDeleteProduct.mockResolvedValue(undefined);

                const result = await mockedService.softDeleteProduct(mockProduct.id);

                expect(result).toEqual(undefined);
                expect(mockedService.softDeleteProduct).toHaveBeenCalledTimes(1);
                expect(mockedService.softDeleteProduct).toHaveBeenCalledWith(mockProduct.id);

            });       

            test("Failure case | Xóa dữ liệu thất bại",async ()=>{
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

                mockedService.softDeleteProduct.mockRejectedValue(new Error('Failed to delete product. Please try again.'));

                await expect(mockedService.softDeleteProduct(mockProduct.id)).rejects.toThrow('Failed to delete product. Please try again.');
                expect(mockedService.softDeleteProduct).toHaveBeenCalledTimes(1);
                expect(mockedService.softDeleteProduct).toHaveBeenCalledWith(mockProduct.id);

            });    
        }); 
    });
});