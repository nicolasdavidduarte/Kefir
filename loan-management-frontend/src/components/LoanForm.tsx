import React, { useState } from "react";
import { createLoan } from "../services/loanService";
import { Loan } from "../types";

const LoanForm: React.FC = () => {
    const [form, setForm] = useState<Loan>({
        customer: 1,
        loanType: 1,
        totalOperationAmount: 5000,
        openingDate: "2025-02-22",
        currency: 2,
        expirationDate: "2025-12-20",
        totalTermDays: 240,
        nextInstallmentDate: "2024-03-18",
        status: 1,
        lastModificationDate: "2024-02-20",
        coreUser: 1
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await createLoan(form);
        alert("Loan created successfully!");
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                name="totalOperationAmount"
                value={form.totalOperationAmount}
                onChange={handleChange}
                placeholder="Total Operation Amount"
                type="number"
            />
            <button type="submit">Create Loan</button>
        </form>
    );
};

export default LoanForm;