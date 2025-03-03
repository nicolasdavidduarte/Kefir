import React from "react";
import { Link } from "react-router-dom"; // Import Link component from react-router-dom

const KefirMain: React.FC = () => {
    return (
        <div className="container mt-5">
            <div className="card">
                <div className="text-white p-4" style={{ backgroundColor: '#1a68bb' }}>
                    <h2 className="mb-0">Main Page</h2>
                </div>
                <div className="card-body">
                    <div className="row">
                        <div className="col-md-6">
                            <Link to="/create-loan" className="btn btn-light w-100 p-3 border" style={{ backgroundColor: "#f8f9fa" }}>
                                <h5 className="mb-0">Loan Creation</h5>
                                <p className="mb-0">Create a new loan</p>
                            </Link>
                        </div>
                        <div className="col-md-6">
                            <Link to="/list-loans" className="btn btn-light w-100 p-3 border" style={{ backgroundColor: "#f8f9fa" }}>
                                <h5 className="mb-0">Loans List</h5>
                                <p className="mb-0">Get all loans in the database</p>
                            </Link>
                        </div>
                        <div className="col-md-6">
                               <Link to="/create-customer" className="btn btn-light w-100 p-3 border" style={{ backgroundColor: "#f8f9fa" }}>
                                   <h5 className="mb-0">Customer Creation</h5>
                                   <p className="mb-0">Add a new customer</p>
                               </Link>
                        </div>
                        <div className="col-md-6">
                            <Link to="/list-customers" className="btn btn-light w-100 p-3 border" style={{ backgroundColor: "#f8f9fa" }}>
                                <h5 className="mb-0">Customers List</h5>
                                <p className="mb-0">Get all customers in the database</p>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default KefirMain;
