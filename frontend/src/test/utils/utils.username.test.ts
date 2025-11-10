import { validation } from '../../utils/utils';

describe('Username Validation Tests', () => {
    
    //* SUCCESS: Username hợp lệ
    describe('K1: Username hợp lệ', () => {
        test('TC1: Username hợp lệ với chữ thường và số', () => {
            expect(validation.validateUsername('user123')).toEqual({ valid: true });
        });

        test('TC2: Username hợp lệ với chữ hoa và số', () => {
            expect(validation.validateUsername('User123')).toEqual({ valid: true });
        });

        test('TC3: Username hợp lệ với dấu chấm', () => {
            expect(validation.validateUsername('john.doe')).toEqual({ valid: true });
        });

        test('TC4: Username hợp lệ với gạch ngang', () => {
            expect(validation.validateUsername('john-doe')).toEqual({ valid: true });
        });

        test('TC5: Username hợp lệ với gạch dưới', () => {
            expect(validation.validateUsername('john_doe')).toEqual({ valid: true });
        });

        test('TC6: Username hợp lệ với tất cả ký tự được phép', () => {
            expect(validation.validateUsername('User.Name-123_test')).toEqual({ valid: true });
        });

        test('TC7: Username hợp lệ với độ dài tối thiểu (3 ký tự)', () => {
            expect(validation.validateUsername('abc')).toEqual({ valid: true });
        });

        test('TC8: Username hợp lệ với độ dài tối đa (50 ký tự)', () => {
            const username = 'a'.repeat(50);
            expect(validation.validateUsername(username)).toEqual({ valid: true });
        });

        test('TC9: Username hợp lệ chỉ có chữ', () => {
            expect(validation.validateUsername('username')).toEqual({ valid: true });
        });

        test('TC10: Username hợp lệ chỉ có số', () => {
            expect(validation.validateUsername('123456')).toEqual({ valid: true });
        });
    });

    //! ERROR: Username is empty
    describe('K2: Username rỗng hoặc chỉ có khoảng trắng', () => {
        test('TC11: Username rỗng - nên trả về lỗi', () => {
            expect(validation.validateUsername('')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được để trống' 
            });
        });

        test('TC12: Username chỉ có 1 khoảng trắng', () => {
            expect(validation.validateUsername(' ')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được để trống' 
            });
        });

        test('TC13: Username chỉ có nhiều khoảng trắng', () => {
            expect(validation.validateUsername('   ')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được để trống' 
            });
        });

        test('TC14: Username chỉ có tab và khoảng trắng', () => {
            expect(validation.validateUsername('\t  \t')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được để trống' 
            });
        });
    });

    //! ERROR: Username too short
    describe('K3: Username quá ngắn (< 3 ký tự)', () => {
        test('TC15: Username có 1 ký tự', () => {
            expect(validation.validateUsername('a')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng phải có ít nhất 3 ký tự' 
            });
        });

        test('TC16: Username có 2 ký tự', () => {
            expect(validation.validateUsername('ab')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng phải có ít nhất 3 ký tự' 
            });
        });

        test('TC17: Username có 2 ký tự số', () => {
            expect(validation.validateUsername('12')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng phải có ít nhất 3 ký tự' 
            });
        });

        test('TC18: Username có 2 ký tự kết hợp', () => {
            expect(validation.validateUsername('a1')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng phải có ít nhất 3 ký tự' 
            });
        });
    });

    //! ERROR: username OOF 
    describe('K4: Username quá dài (> 50 ký tự)', () => {
        test('TC19: Username có 51 ký tự', () => {
            const username = 'a'.repeat(51);
            expect(validation.validateUsername(username)).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được vượt quá 50 ký tự' 
            });
        });

        test('TC20: Username có 60 ký tự', () => {
            const username = 'a'.repeat(60);
            expect(validation.validateUsername(username)).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được vượt quá 50 ký tự' 
            });
        });

        test('TC21: Username có 100 ký tự', () => {
            const username = 'a'.repeat(100);
            expect(validation.validateUsername(username)).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được vượt quá 50 ký tự' 
            });
        });

        test('TC22: Username có 51 ký tự hỗn hợp', () => {
            const username = 'User123_test.name-' + 'a'.repeat(34);
            expect(validation.validateUsername(username)).toEqual({ 
                valid: false, 
                error: 'Tên người dùng không được vượt quá 50 ký tự' 
            });
        });
    });

    //! ERROR: Ký tự đặc biệt  ko hợp lệ
    describe('K5: Username có ký tự đặc biệt không hợp lệ', () => {
        test('TC23: Username có ký tự @ không hợp lệ', () => {
            expect(validation.validateUsername('user@name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC24: Username có ký tự # không hợp lệ', () => {
            expect(validation.validateUsername('user#123')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC25: Username có ký tự $ không hợp lệ', () => {
            expect(validation.validateUsername('user$name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC26: Username có ký tự % không hợp lệ', () => {
            expect(validation.validateUsername('user%123')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC27: Username có ký tự & không hợp lệ', () => {
            expect(validation.validateUsername('user&name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC28: Username có ký tự * không hợp lệ', () => {
            expect(validation.validateUsername('user*123')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC29: Username có khoảng trắng ở giữa không hợp lệ', () => {
            expect(validation.validateUsername('user name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC30: Username có ký tự ! không hợp lệ', () => {
            expect(validation.validateUsername('user!name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC31: Username có ký tự + không hợp lệ', () => {
            expect(validation.validateUsername('user+name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC32: Username có ký tự = không hợp lệ', () => {
            expect(validation.validateUsername('user=123')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC33: Username có ký tự ~ không hợp lệ', () => {
            expect(validation.validateUsername('user~name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC34: Username có ký tự ^ không hợp lệ', () => {
            expect(validation.validateUsername('user^123')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC35: Username có ký tự ( không hợp lệ', () => {
            expect(validation.validateUsername('user(name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC36: Username có ký tự ) không hợp lệ', () => {
            expect(validation.validateUsername('user)123')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC37: Username có ký tự [ không hợp lệ', () => {
            expect(validation.validateUsername('user[name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC38: Username có nhiều ký tự đặc biệt không hợp lệ', () => {
            expect(validation.validateUsername('user@#$%name')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC39: Username có ký tự tiếng Việt không hợp lệ', () => {
            expect(validation.validateUsername('ngườidùng')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });

        test('TC40: Username có emoji không hợp lệ', () => {
            expect(validation.validateUsername('user😀')).toEqual({ 
                valid: false, 
                error: 'Tên người dùng chỉ được chứa chữ cái, số, dấu chấm, gạch ngang và gạch dưới' 
            });
        });
    });
});
