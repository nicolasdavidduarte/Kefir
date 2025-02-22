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
