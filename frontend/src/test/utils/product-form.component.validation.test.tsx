import { render, screen, waitFor, act, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import ProductForm from "../../components/products/product-form";
import "@testing-library/jest-dom";

jest.mock("../../services/categoryService", () => ({
  categoryService: {
    getAllCategories: jest.fn().mockResolvedValue([
      { id: 1, name: "Laptop", description: "desc" },
      { id: 2, name: "Gaming", description: "desc" },
    ]),
  },
}));
jest.mock("../../services/productService", () => ({
  productService: {
    createProduct: jest.fn(),
    updateProduct: jest.fn(),
  },
}));

afterEach(() => {
  jest.clearAllMocks();
});

describe("ProductForm Component Validation Integration", () => {
  const defaultProps = {
    product: null,
    onCancel: jest.fn(),
  };

  test("Không submit khi tên sản phẩm rỗng (native validation)", async () => {
    const onSubmit = jest.fn();
    render(<ProductForm {...defaultProps} onSubmit={onSubmit} />);
    await waitFor(() => expect(screen.getByLabelText("Loại Laptop *")).toBeInTheDocument());
    const nameInput = screen.getByLabelText("Tên Laptop *");
    await act(async () => {
      fireEvent.change(nameInput, { target: { value: "" } });
    });
    const submitBtn = screen.getByRole("button", { name: /Thêm Laptop/i });
    await act(async () => {
      await userEvent.click(submitBtn);
    });
    expect(onSubmit).not.toHaveBeenCalled();
  });

  test("Không submit khi giá trị âm (native validation)", async () => {
    const onSubmit = jest.fn();
    render(<ProductForm {...defaultProps} onSubmit={onSubmit} />);
    await waitFor(() => expect(screen.getByLabelText("Loại Laptop *")).toBeInTheDocument());
    const priceInput = screen.getByLabelText("Giá ($) *");
    await act(async () => {
      fireEvent.change(priceInput, { target: { value: "-1" } });
    });
    const submitBtn = screen.getByRole("button", { name: /Thêm Laptop/i });
    await act(async () => {
      await userEvent.click(submitBtn);
    });
    expect(onSubmit).not.toHaveBeenCalled();
  });

  test("Không submit khi số lượng âm (native validation)", async () => {
    const onSubmit = jest.fn();
    render(<ProductForm {...defaultProps} onSubmit={onSubmit} />);
    await waitFor(() => expect(screen.getByLabelText("Loại Laptop *")).toBeInTheDocument());
    const quantityInput = screen.getByLabelText("Số Lượng *");
    await act(async () => {
      fireEvent.change(quantityInput, { target: { value: "-5" } });
    });
    const submitBtn = screen.getByRole("button", { name: /Thêm Laptop/i });
    await act(async () => {
      await userEvent.click(submitBtn);
    });
    expect(onSubmit).not.toHaveBeenCalled();
  });

  test("Không submit khi mô tả vượt quá 500 ký tự (native validation)", async () => {
    const onSubmit = jest.fn();
    render(<ProductForm {...defaultProps} onSubmit={onSubmit} />);
    await waitFor(() => expect(screen.getByLabelText("Loại Laptop *")).toBeInTheDocument());
    const descriptionInput = screen.getByLabelText('Mô Tả Laptop');
    await act(async () => {
      fireEvent.change(descriptionInput, { target: { value: 'a'.repeat(501) } });
    });
    const submitBtn = screen.getByRole('button', { name: /Thêm Laptop/i });
    await act(async () => {
      await userEvent.click(submitBtn);
    });
    expect(onSubmit).not.toHaveBeenCalled();
  });

  test("Không submit khi chưa chọn thể loại (native validation)", async () => {
    const onSubmit = jest.fn();
    render(<ProductForm {...defaultProps} onSubmit={onSubmit} />);
    await waitFor(() => expect(screen.getByLabelText("Loại Laptop *")).toBeInTheDocument());
    const categorySelect = screen.getByLabelText('Loại Laptop *');
    await act(async () => {
      fireEvent.change(categorySelect, { target: { value: '' } });
    });
    const submitBtn = screen.getByRole('button', { name: /Thêm Laptop/i });
    await act(async () => {
      await userEvent.click(submitBtn);
    });
    expect(onSubmit).not.toHaveBeenCalled();
  });
});
