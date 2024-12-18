export enum UserRole {
    ROLE_USER = 'Пользователь',
    ROLE_ADMIN = 'Администратор',
    ROLE_ACCIDENT_COMMISSAR = 'Аварийный комиссар',
}

export enum DocumentType {
    PASSPORT = 'Паспорт',
    DRIVING_LICENSE = 'Водительское удостоверение'
}

export enum VerificationStatuses {
    PENDING = 'PENDING',
    REJECTED = 'REJECTED',
    VERIFIED = 'VERIFIED'
}

export interface RegisterData {
    username: string;
    password: string;
    role: string,
    email: string,
    name: string,
    lastName: string,
    phone: string,
    experience: number | undefined,
    birthDate: string
}