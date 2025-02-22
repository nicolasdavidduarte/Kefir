/*import React from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
*/

import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoanList from "./components/LoanList";
import LoanForm from "./components/LoanForm";

const App: React.FC = () => {
    return (
        <Router>
            <div>
                <h1>Loan Management System</h1>
                <Routes>
                    <Route path="/" element={<LoanList />} />
                    <Route path="/create-loan" element={<LoanForm />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
