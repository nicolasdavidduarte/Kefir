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
        coreUser: 1,
        closedDate: "", // Optional field
        closedCode: 0 // Optional field
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
            {/* Customer */}
            <input
                name="customer"
                value={form.customer}
                onChange={handleChange}
                placeholder="Customer ID"
                type="number"
            />
            {/* Loan Type */}
            <input
                name="loanType"
                value={form.loanType}
                onChange={handleChange}
                placeholder="Loan Type"
                type="number"
            />
            {/* Total Operation Amount */}
            <input
                name="totalOperationAmount"
                value={form.totalOperationAmount}
                onChange={handleChange}
                placeholder="Total Operation Amount"
                type="number"
            />
            {/* Opening Date */}
            <input
                name="openingDate"
                value={form.openingDate}
                onChange={handleChange}
                placeholder="Opening Date"
                type="date"
            />
            {/* Currency */}
            <input
                name="currency"
                value={form.currency}
                onChange={handleChange}
                placeholder="Currency"
                type="number"
            />
            {/* Expiration Date */}
            <input
                name="expirationDate"
                value={form.expirationDate}
                onChange={handleChange}
                placeholder="Expiration Date"
                type="date"
            />
            {/* Total Term Days */}
            <input
                name="totalTermDays"
                value={form.totalTermDays}
                onChange={handleChange}
                placeholder="Total Term Days"
                type="number"
            />
            {/* Next Installment Date */}
            <input
                name="nextInstallmentDate"
                value={form.nextInstallmentDate}
                onChange={handleChange}
                placeholder="Next Installment Date"
                type="date"
            />
            {/* Status */}
            <input
                name="status"
                value={form.status}
                onChange={handleChange}
                placeholder="Status"
                type="number"
            />
            {/* Last Modification Date */}
            <input
                name="lastModificationDate"
                value={form.lastModificationDate}
                onChange={handleChange}
                placeholder="Last Modification Date"
                type="date"
            />
            {/* Core User */}
            <input
                name="coreUser"
                value={form.coreUser}
                onChange={handleChange}
                placeholder="Core User"
                type="number"
            />
            {/* Optional Fields */}
            {/* Closed Date */}
            <input
                name="closedDate"
                value={form.closedDate || ""}  // Fallback to empty string if null or undefined
                onChange={handleChange}
                placeholder="Closed Date"
                type="date"
            />
            {/* Closed Code */}
            <input
                name="closedCode"
                value={form.closedCode ?? 0}  // Fallback to 0 if null or undefined
                onChange={handleChange}
                placeholder="Closed Code"
                type="number"
            />

            {/* Submit Button */}
            <button type="submit">Create Loan</button>
        </form>
    );
};

export default LoanForm;
