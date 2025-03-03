export interface Loan {
    id?: number;
    customer: number;
    loanType: number;
    totalOperationAmount: number;
    openingDate: string;
    currency: number;
    expirationDate: string;
    totalTermDays: number;
    closedDate?: string | null;
    closedCode?: number | null;
    nextInstallmentDate: string;
    status: number;
    lastModificationDate: string;
    coreUser: number;
}

export interface Customer {
    id?: number;
    name: string | null;
    firstName1: string | null;
    lastName1: string | null;
    firstName2: string | null;
    lastName2: string | null;
    nameShort: string | null;
    personType: number;
    customerType: number;
    status: number;
    lastModificationDate: string;
    coreUser: number;
}
