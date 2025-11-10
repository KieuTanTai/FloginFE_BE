import { validation } from "../../utils/utils";

describe('Password validation test', () => {
    //* SUCCESS: Password hợp lệ
    describe('K1: Password hợp lệ', () => {
        test('TC1: password hợp lệ với chữ thường và số', () => {
            expect(validation.validatePassword('admin123')).toEqual({ valid: true });
        });

        test('K2: Password hợp lệ với chữ hoa và chữ số', () => {
            expect(validation.validatePassword('Admin123')).toEqual({ valid: true });
        });
    });
})