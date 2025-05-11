import React from 'react';
import '../Styles/CustomInput.css';

const CustomInput = ({ label, type = 'text', value, onChange, placeholder }) => (
  <div className="custom-input-container">
    {label && <label className="custom-input-label">{label}</label>}
    <input
      className="custom-input"
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
    />
  </div>
);

export default CustomInput; 