import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import LoginForm from "../../components/auth/login-form";
import { accountService } from "../../services/accountService";
import '@testing-library/jest-dom';

jest.mock("../../services/accountService", () => ({
  accountService: {
    login: jest.fn(),
  },
}));

describe("LoginForm Mock Tests", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test("Mock: Login thành công", async () => {
    (accountService.login as jest.Mock).mockResolvedValue({
      id: 1,
      username: "testuser",
      email: "test@mail.com",
    });

    render(<LoginForm onLoginSuccess={jest.fn()} />);

    fireEvent.change(screen.getByLabelText(/tên đăng nhập/i), {
      target: { value: "testuser" },
    });
    fireEvent.change(screen.getByLabelText(/mật khẩu/i), {
      target: { value: "Test123" },
    });
    fireEvent.click(screen.getByRole("button", { name: /đăng nhập/i }));

    await waitFor(() => {
      expect(accountService.login).toHaveBeenCalledWith({
        username: "testuser",
        password: "Test123",
      });
      // Kiểm tra hiển thị thông báo thành công nếu có
      // expect(screen.getByText(/thành công/i)).toBeInTheDocument();
    });
  });

  test("Mock: Login thất bại", async () => {
    (accountService.login as jest.Mock).mockRejectedValue(new Error("Sai thông tin"));

    render(<LoginForm onLoginSuccess={jest.fn()} />);

    fireEvent.change(screen.getByLabelText(/tên đăng nhập/i), {
      target: { value: "testuser" },
    });
    fireEvent.change(screen.getByLabelText(/mật khẩu/i), {
      target: { value: "sai" },
    });
    fireEvent.click(screen.getByRole("button", { name: /đăng nhập/i }));

    expect(await screen.findByText(/sai thông tin/i)).toBeInTheDocument();
  });
});