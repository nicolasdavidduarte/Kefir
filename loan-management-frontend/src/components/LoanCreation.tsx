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
        closedDate: "",
        closedCode: 0
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
        <div className="container mt-5">
            <div className="card">
                 <div className="text-white p-4" style={{ backgroundColor: '#1a68bb' }}>
                    <h2 className="mb-0">Loan Creation</h2>
                </div>
                <div className="card-body">
                    <form onSubmit={handleSubmit}>
                        <div className="row g-3">
                            {Object.entries(form).map(([key, value]) => (
                                <div key={key} className="col-md-6">
                                    <div className="mb-3">
                                        <label htmlFor={key} className="form-label">{key}</label>
                                        <input
                                            id={key}
                                            name={key}
                                            value={value as string | number}
                                            onChange={handleChange}
                                            placeholder={key}
                                            type={
                                                key.toLowerCase().includes("date") ? "date" :
                                                typeof value === "number" ? "number" : "text"
                                            }
                                            className="form-control"
                                        />
                                    </div>
                                </div>
                            ))}
                            <div className="col-12 d-flex justify-content-end">
                                <button type="submit" className="btn btn-primary">Create Loan</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default LoanForm;
