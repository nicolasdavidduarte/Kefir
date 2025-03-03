import axios from "axios"; // for API calls
import { Customer } from "../types";

const API_URL = "http://localhost:8080/api/customer";

// Get all customer
export const getCustomers = async (): Promise<Customer[]> => {
    const response = await axios.get(API_URL);
    return response.data;
};

// Get customer by ID
export const getCustomerById = async (id: number): Promise<Customer> => {
    const response = await axios.post(`${API_URL}/getCustomerById`, { id });
    return response.data;
};

// Create a new customer
export const createCustomer = async (customer: Customer): Promise<Customer> => {
    const response = await axios.post(`${API_URL}`, customer);
    return response.data;
};

// Delete a customer by ID
export const deleteCustomer = async (id: number): Promise<string> => {
    const response = await axios.delete(`${API_URL}/deleteCustomer`, { data: { id } });
    return response.data;
};

// Update a customer
export const updateCustomer = async (customer: Customer): Promise<Customer> => {
    const response = await axios.post(`${API_URL}/updateCustomer`, customer);
    return response.data;
};
