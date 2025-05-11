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
    if (password !== confirmPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }
    // Obtener usuarios existentes
    const users = JSON.parse(localStorage.getItem('users') || '[]');
    // Validar si el correo ya existe
    if (users.some(user => user.email === email)) {
      alert('El correo ya está registrado');
      return;
    }
    // Guardar nuevo usuario
    users.push({ email, password });
    localStorage.setItem('users', JSON.stringify(users));
    alert('Registro exitoso. Ahora puedes iniciar sesión.');
    window.location.href = '/login';
  };

  const handleLoginRedirect = () => {
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
