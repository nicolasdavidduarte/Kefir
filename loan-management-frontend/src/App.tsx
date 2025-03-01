import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import KefirMain from "./components/KefirMain";
import LoanList from "./components/LoanList";
import LoanForm from "./components/LoanCreation";

const App: React.FC = () => {
    return (
        <Router>
            <div className="text-white p-4" style={{ backgroundColor: '#1a68bb' }}>
                  <h2 className="h3 fw-bold">Kefir Loan Manager</h2>
             </div>


            <Routes>
                <Route path="/" element={<Navigate to="/main" />} />

                <Route path="/main" element={<KefirMain />} />
                <Route path="/list-loans" element={<LoanList />} />
                <Route path="/create-loan" element={<LoanForm />} />
            </Routes>
        </Router>
    );
};

export default App;