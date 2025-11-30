import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import LoginForm from "../../components/auth/login-form";
import { accountService } from "../../services/accountService";
import "@testing-library/jest-dom";

jest.mock("../../services/accountService");

describe("LoginForm Integration Test", () => {
    const mockOnSuccess = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    test("Integration: nhập form → gọi API thành công → onLoginSuccess được gọi", async () => {
        const mockAccount = {
            id: 1,
            username: "duy",
            role: "admin",
        };

        (accountService.login as jest.Mock).mockResolvedValue(mockAccount);

        render(<LoginForm onLoginSuccess={mockOnSuccess} />);

        // ---- User nhập username & password ----
        fireEvent.change(screen.getByPlaceholderText("Nhập tên đăng nhập"), {
            target: { value: "duy" },
        });
        fireEvent.change(screen.getByPlaceholderText("Nhập mật khẩu"), {
            target: { value: "123456" },
        });

        // ---- User nhấn nút đăng nhập ----
        fireEvent.click(screen.getByRole("button", { name: "Đăng Nhập" }));

        // ---- UI phải hiện loading ----
        expect(screen.getByText("Đang đăng nhập...")).toBeInTheDocument();

        // ---- API resolve → onSuccess được gọi ----
        await waitFor(() => {
            expect(mockOnSuccess).toHaveBeenCalledTimes(1);
            expect(mockOnSuccess).toHaveBeenCalledWith(mockAccount);
        });
    });

    test("Integration: nhập form → API trả lỗi → hiển thị lỗi", async () => {
        (accountService.login as jest.Mock).mockRejectedValue(
            new Error("Sai thông tin đăng nhập")
        );

        render(<LoginForm onLoginSuccess={mockOnSuccess} />);

        fireEvent.change(screen.getByPlaceholderText("Nhập tên đăng nhập"), {
            target: { value: "sai-user" },
        });
        fireEvent.change(screen.getByPlaceholderText("Nhập mật khẩu"), {
            target: { value: "sai-pass" },
        });

        fireEvent.click(screen.getByRole("button", { name: "Đăng Nhập" }));

        // ---- UI phải hiện message lỗi từ API ----
        expect(
            await screen.findByText("Sai thông tin đăng nhập")
        ).toBeInTheDocument();

        expect(mockOnSuccess).not.toHaveBeenCalled();
    });

    test("Integration: để trống username/password → hiển thị lỗi validate", async () => {
        render(<LoginForm onLoginSuccess={mockOnSuccess} />);

        fireEvent.click(screen.getByRole("button", { name: "Đăng Nhập" }));

        expect(
            await screen.findByText("Vui lòng nhập đầy đủ thông tin")
        ).toBeInTheDocument();
        expect(mockOnSuccess).not.toHaveBeenCalled();
    });

    test("Integration: nút Đăng Nhập disabled khi đang loading", async () => {
        let resolve: any;
        const pendingPromise = new Promise((res) => (resolve = res));

        (accountService.login as jest.Mock).mockReturnValue(pendingPromise);

        render(<LoginForm onLoginSuccess={mockOnSuccess} />);

        fireEvent.change(screen.getByPlaceholderText("Nhập tên đăng nhập"), {
            target: { value: "duy" },
        });
        fireEvent.change(screen.getByPlaceholderText("Nhập mật khẩu"), {
            target: { value: "123456" },
        });

        const btn = screen.getByRole("button", { name: "Đăng Nhập" });

        fireEvent.click(btn);

        expect(btn).toBeDisabled();

        resolve(); // kết thúc promise
    });
});
