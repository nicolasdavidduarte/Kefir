import React, { useState } from "react";
import { createCustomer } from "../services/customerService";
import { Customer } from "../types";

const CustomerCreation: React.FC = () => {
    const [form, setForm] = useState<Customer>({
        name: "",
        firstName1: "",
        lastName1: "",
        firstName2: "",
        lastName2: "",
        nameShort: "",
        personType: 1,
        customerType: 1,
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
        const customerDTO = { ...form, lastModificationDate: new Date().toISOString() }; // Ensure date is in ISO format
        await createCustomer(form);
        alert("Customer created successfully!");
    };

    return (
        <div className="container mt-5">
            <div className="card">
                 <div className="text-white p-4" style={{ backgroundColor: '#1a68bb' }}>
                    <h2 className="mb-0">Customer Creation</h2>
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
                                <button type="submit" className="btn btn-primary">Create Customer</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default CustomerCreation;