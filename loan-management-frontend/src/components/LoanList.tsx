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
        <div>
            <h2>Loan List</h2>
            <ul>
                {loans.map((loan) => (
                    <li key={loan.id}>
                        Loan ID: {loan.id}, Amount: {loan.totalOperationAmount}
                        <button onClick={() => handleDelete(loan.id!)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default LoanList;