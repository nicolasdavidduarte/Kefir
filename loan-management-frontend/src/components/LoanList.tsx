import React, { useEffect, useState } from "react";
import { getLoans, deleteLoan } from "../services/loanService";
import { Loan } from "../types";

const LoanList: React.FC = () => {
    const [loans, setLoans] = useState<Loan[]>([]);

    useEffect(() => {
        fetchLoans();
    }, []);

    const fetchLoans = async () => {
        const data = await getLoans();
        setLoans(data);
    };

    const handleDelete = async (id: number) => {
        await deleteLoan(id);
        fetchLoans(); // Refresh the list
    };

    return (
        <div className="container mt-5">
            <div className="card">
                 <div className="text-white p-4" style={{ backgroundColor: '#1a68bb' }}>
                    <h2 className="mb-0">Loan List</h2>
                </div>
                <div className="card-body">
                    <div className="table-responsive">
                        <table className="table table-striped">
                            <thead>
                                <tr>
                                    <th>Loan ID</th>
                                    <th>Amount</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {loans.map((loan) => (
                                    <tr key={loan.id}>
                                        <td>{loan.id}</td>
                                        <td>{loan.totalOperationAmount}</td>
                                        <td>
                                            <button
                                                className="btn btn-danger"
                                                onClick={() => handleDelete(loan.id!)}
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

export default LoanList;
