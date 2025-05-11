import React, { useState } from 'react';
import CustomInput from '../components/CustomInput';
import CustomButton from '../components/CustomButton';
import '../Styles/AuthPages.css';
import loginImg from '../assets/login-img.png';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = (e) => {
    e.preventDefault();
    // Lógica de autenticación aquí
    alert('Iniciar sesión (demo)');
  };

  const handleRegisterRedirect = () => {
    // Redirigir a la página de registro
    window.location.href = '/register';
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-title">CHOP CHOP</div>
        <img src={loginImg} alt="login" className="auth-image" />
        <div className="auth-subtitle">INICIO DE SESION</div>
        <form className="auth-form" onSubmit={handleLogin}>
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
          <CustomButton text="Iniciar Sesion" type="submit" bgColor="#43B02A" />
        </form>
        <CustomButton text="Crear Cuenta" bgColor="#43B02A" onClick={handleRegisterRedirect} />
      </div>
    </div>
  );
};

export default Login;
