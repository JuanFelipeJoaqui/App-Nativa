import React from 'react';
import '../Styles/CustomButton.css';

const CustomButton = ({ text, onClick, bgColor = '#43B02A', type = 'button' }) => (
  <button
    className="custom-button"
    style={{ backgroundColor: bgColor }}
    onClick={onClick}
    type={type}
  >
    {text}
  </button>
);

export default CustomButton; 