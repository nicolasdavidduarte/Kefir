import React, { useEffect, useState } from "react";
import { getCustomers, deleteCustomer } from "../services/customerService";
import { Customer } from "../types";

const CustomerList: React.FC = () => {
    const [customers, setCustomer] = useState<Customer[]>([]);

    useEffect(() => {
        fetchCustomer();
    }, []);

    const fetchCustomer = async () => {
        const data = await getCustomers();
        setCustomer(data);
    };

    const handleDelete = async (id: number) => {
        await deleteCustomer(id);
        fetchCustomer(); // Refresh the list
    };

    return (
        <div className="container mt-5">
            <div className="card">
                 <div className="text-white p-4" style={{ backgroundColor: '#1a68bb' }}>
                    <h2 className="mb-0">Customer List</h2>
                </div>
                <div className="card-body">
                    <div className="table-responsive">
                        <table className="table table-striped">
                            <thead>
                                <tr>
                                    <th>Customer ID</th>
                                    <th>Name</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {customers.map((customer) => (
                                    <tr key={customer.id}>
                                        <td>{customer.id}</td>
                                        <td>{customer.nameShort}</td>
                                        <td>
                                            <button
                                                className="btn btn-danger"
                                                onClick={() => handleDelete(customer.id!)}
                                            >
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CustomerList;
