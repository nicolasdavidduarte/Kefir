import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import KefirMain from "./components/KefirMain";
import LoanList from "./components/LoanList";
import LoanCreation from "./components/LoanCreation";
import CustomerCreation from "./components/CustomerCreation";
import CustomerList from "./components/CustomerList";

const App: React.FC = () => {
    return (
        <Router>
            <div className="text-white p-4" style={{ backgroundColor: '#1a68bb' }}>
                  <h2 className="h3 fw-bold">Kefir Loan Manager</h2>
             </div>
            <Routes>
                <Route path="/" element={<Navigate to="/main" />} />

                <Route path="/main" element={<KefirMain />} />
                <Route path="/create-loan" element={<LoanCreation />} />
                <Route path="/list-loans" element={<LoanList />} />

                <Route path="/create-customer" element={<CustomerCreation />} />
                <Route path="/list-customers" element={<CustomerList />} />
            </Routes>
        </Router>
    );
};

export default App;