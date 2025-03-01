import React from "react";

// Card Component
export const Card: React.FC<{ children: React.ReactNode; className?: string }> = ({ children, className = "" }) => {
    return (
        <div className={`bg-white p-4 rounded-2xl shadow-md ${className}`}>
            {children}
        </div>
    );
};

// CardContent Component
export const CardContent: React.FC<{ children: React.ReactNode; className?: string }> = ({ children, className = "" }) => {
    return <div className={`p-4 ${className}`}>{children}</div>;
};