import React, { useState } from 'react';
import CustomInput from '../components/CustomInput';
import CustomButton from '../components/CustomButton';
import '../Styles/AuthPages.css';
import registerImg from '../assets/register-img.png';

const Register = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleRegister = (e) => {
    e.preventDefault();
    // Lógica de registro aquí
    if (password !== confirmPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }
    alert('Registro (demo)');
  };

  const handleLoginRedirect = () => {
    // Redirigir a la página de login
    window.location.href = '/login';
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-title">CHOP CHOP</div>
        <img src={registerImg} alt="register" className="auth-image" />
        <div className="auth-subtitle">REGISTRO</div>
        <form className="auth-form" onSubmit={handleRegister}>
          <CustomInput
            type="email"
            placeholder="Correo"
            value={email}
            onChange={e => setEmail(e.target.value)}
          />
          <CustomInput
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
          <CustomInput
            type="password"
            placeholder="Confirmar contraseña"
            value={confirmPassword}
            onChange={e => setConfirmPassword(e.target.value)}
          />
          <CustomButton text="Registrarse" type="submit" bgColor="#FFA800" />
        </form>
        <div className="auth-link" onClick={handleLoginRedirect}>
          ¿Ya tienes cuenta? Inicia sesión
        </div>
      </div>
    </div>
  );
};

export default Register;
