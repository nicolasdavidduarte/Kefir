import axios from "axios"; // for API calls
import { Loan } from "../types";

const API_URL = "http://localhost:8080/api/loan";

// Get all loans
export const getLoans = async (): Promise<Loan[]> => {
    const response = await axios.get(API_URL);
    return response.data;
};

// Get loan by ID
export const getLoanById = async (id: number): Promise<Loan> => {
    const response = await axios.post(`${API_URL}/getLoanById`, { id });
    return response.data;
};

// Create a new loan
export const createLoan = async (loan: Loan): Promise<Loan> => {
    const response = await axios.post(`${API_URL}/createLoan`, loan);
    return response.data;
};

// Delete a loan by ID
export const deleteLoan = async (id: number): Promise<string> => {
    const response = await axios.post(`${API_URL}/deleteLoan`, { id });
    return response.data;
};

// Update a loan
export const updateLoan = async (loan: Loan): Promise<Loan> => {
    const response = await axios.post(`${API_URL}/updateLoan`, loan);
    return response.data;
};
