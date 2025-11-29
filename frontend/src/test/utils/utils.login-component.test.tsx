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
    describe("Render tests", () => {
        test("hiển thị form đăng nhập đúng", () => {
            render(<LoginForm onLoginSuccess={mockOnSuccess} />);
            expect(screen.getByRole("heading",{name:"Đăng Nhập"})).toBeInTheDocument();
            expect(screen.getByText("Quản Lý Sách")).toBeInTheDocument();
            expect(screen.getByPlaceholderText("Nhập tên đăng nhập")).toBeInTheDocument();
            expect(screen.getByPlaceholderText("Nhập mật khẩu")).toBeInTheDocument();
            expect(screen.getByRole("button", { name: "Đăng Nhập" })).toBeInTheDocument();
        });
    });

    describe("User interaction tests", () => {
        test("kiểm tra nút đang nhập bấm được và hiển thị lỗi khi để trống username hoặc password", async () => {
            render(<LoginForm onLoginSuccess={mockOnSuccess} />);

            fireEvent.click(screen.getByRole("button", { name: "Đăng Nhập" }));

            expect(await screen.findByText("Vui lòng nhập đầy đủ thông tin"))
                .toBeInTheDocument();
            expect(mockOnSuccess).not.toHaveBeenCalled();
            
        });

        test("kiểm tra form gửi đúng dữ liệu khi nhập username và password", async () => {
            render(<LoginForm onLoginSuccess={mockOnSuccess} />);
            fireEvent.change(screen.getByPlaceholderText("Nhập tên đăng nhập"), {
                target: { value: "duy" },
            });
            fireEvent.change(screen.getByPlaceholderText("Nhập mật khẩu"), {
                target: { value: "123456" },
            });
            expect(screen.getByPlaceholderText("Nhập tên đăng nhập")).toHaveValue("duy");
            expect(screen.getByPlaceholderText("Nhập mật khẩu")).toHaveValue("123456");
        });

        test("nút Login disabled khi đang loading", async () => {
            // mock pending promise
            let resolve: any;
            const loginPromise = new Promise((res) => (resolve = res));

            (accountService.login as jest.Mock).mockReturnValue(loginPromise);

            render(<LoginForm onLoginSuccess={mockOnSuccess} />);

            fireEvent.change(screen.getByPlaceholderText("Nhập tên đăng nhập"), {
                target: { value: "duy" },
            });

            fireEvent.change(screen.getByPlaceholderText("Nhập mật khẩu"), {
                target: { value: "123456" },
            });

            const btn = screen.getByRole("button", { name: "Đăng Nhập" });
            fireEvent.click(btn);

            // đang loading phải disable
            expect(btn).toBeDisabled();

            resolve(); // hoàn tất promise
        });
    });

    describe("Test error handling và success messages", () => {
            test("gọi API login thành công và gọi onLoginSuccess()", async () => {
                const mockAccount = {
                    id: 1,
                    username: "duy",
                    role: "admin"
                };

            (accountService.login as jest.Mock).mockResolvedValue(mockAccount);

            render(<LoginForm onLoginSuccess={mockOnSuccess} />);

            fireEvent.change(screen.getByPlaceholderText("Nhập tên đăng nhập"), {
                target: { value: "duy" },
            });

            fireEvent.change(screen.getByPlaceholderText("Nhập mật khẩu"), {
                target: { value: "123456" },
            });

            fireEvent.click(screen.getByRole("button", { name: "Đăng Nhập" }));

            expect(screen.getByText("Đang đăng nhập...")).toBeInTheDocument();

            await waitFor(() => expect(mockOnSuccess).toHaveBeenCalledTimes(1));

            expect(mockOnSuccess).toHaveBeenCalledWith(mockAccount);
        });

        test("hiển thị lỗi khi API login thất bại", async () => {
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

            expect(
                await screen.findByText("Sai thông tin đăng nhập")
            ).toBeInTheDocument();

            expect(mockOnSuccess).not.toHaveBeenCalled();
        });

        test("nút Login disabled khi đang loading", async () => {
            let resolve: any;
            const loginPromise = new Promise((res) => (resolve = res));

            (accountService.login as jest.Mock).mockReturnValue(loginPromise);

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

            resolve();
        });
    });
});
