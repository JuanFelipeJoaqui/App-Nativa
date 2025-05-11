import React, { useState } from 'react';
import '../Styles/PrincipalListas.css';
import emptyListImg from '../assets/empty-list.png';
import addImg from '../assets/add.png';

const PrincipalListas = () => {
  const [showCreate, setShowCreate] = useState(false);
  const [nuevaLista, setNuevaLista] = useState('');
  const [listas, setListas] = useState(() => {
    // Cargar listas del usuario logueado
    const user = JSON.parse(localStorage.getItem('loggedUser'));
    if (!user) return [];
    const allLists = JSON.parse(localStorage.getItem('listas') || '{}');
    return allLists[user.email] || [];
  });
  const [selectedTab, setSelectedTab] = useState('listas');

  // Filtrar favoritos si está seleccionado
  const listasToShow = selectedTab === 'favoritos'
    ? listas.filter(l => l.favorito)
    : listas;

  const handleAddLista = () => {
    setShowCreate(true);
  };

  const handleGuardarLista = (e) => {
    e.preventDefault();
    if (nuevaLista.trim() === '') return;
    const nueva = { nombre: nuevaLista.trim(), favorito: false };
    const nuevasListas = [...listas, nueva];
    setListas(nuevasListas);
    setNuevaLista('');
    setShowCreate(false);
    // Guardar en localStorage por usuario
    const user = JSON.parse(localStorage.getItem('loggedUser'));
    const allLists = JSON.parse(localStorage.getItem('listas') || '{}');
    allLists[user.email] = nuevasListas;
    localStorage.setItem('listas', JSON.stringify(allLists));
  };

  const handleTab = (tab) => {
    setSelectedTab(tab);
  };

  const toggleFavorito = (idx) => {
    const nuevasListas = listas.map((l, i) => i === idx ? { ...l, favorito: !l.favorito } : l);
    setListas(nuevasListas);
    // Guardar en localStorage por usuario
    const user = JSON.parse(localStorage.getItem('loggedUser'));
    const allLists = JSON.parse(localStorage.getItem('listas') || '{}');
    allLists[user.email] = nuevasListas;
    localStorage.setItem('listas', JSON.stringify(allLists));
  };

  return (
    <div className="main-listas-container">
      <div className="header-listas">LISTAS</div>
      {listasToShow.length === 0 && !showCreate && (
        <div className="empty-listas">
          Aún no hay listas<br />para mostrar.<br />¡Crea una!
          <img src={emptyListImg} alt="Sin listas" />
        </div>
      )}
      {showCreate && (
        <form className="create-lista-card" onSubmit={handleGuardarLista}>
          <div className="create-lista-title">Crear nueva lista</div>
          <input
            className="create-lista-input"
            type="text"
            placeholder="Nueva lista"
            value={nuevaLista}
            onChange={e => setNuevaLista(e.target.value)}
            autoFocus
          />
          <button className="create-lista-btn" type="submit">Guardar</button>
        </form>
      )}
      {/* Mostrar listas */}
      {listasToShow.length > 0 && !showCreate && (
        <div style={{ margin: '18px' }}>
          {listasToShow.map((l, idx) => (
            <div key={idx} style={{
              background: '#fff',
              borderRadius: 12,
              boxShadow: '0 1px 6px rgba(67,176,42,0.08)',
              padding: '14px 16px',
              marginBottom: 12,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              border: l.favorito ? '2px solid #FFA800' : '2px solid #E0E0E0',
            }}>
              <span style={{ fontWeight: 500, color: '#222', fontSize: 17 }}>{l.nombre}</span>
              <span
                style={{ cursor: 'pointer', fontSize: 24, transition: 'color 0.2s', color: l.favorito ? '#e53935' : '#bbb' }}
                title={l.favorito ? 'Quitar de favoritos' : 'Marcar como favorito'}
                onClick={() => toggleFavorito(idx)}
              >{l.favorito ? '❤️' : '🤍'}</span>
            </div>
          ))}
        </div>
      )}
      <button className="fab-add-lista" onClick={handleAddLista} title="Crear lista">
        <img src={addImg} alt="Añadir" style={{ width: 36, height: 36 }} />
      </button>
      <nav className="bottom-nav">
        <div
          className={`bottom-nav-item ${selectedTab === 'listas' ? 'selected' : ''}`}
          onClick={() => handleTab('listas')}
        >
          <img src={addImg} alt="Listas" style={{ width: 22, height: 22, filter: selectedTab === 'listas' ? 'grayscale(0)' : 'grayscale(1)' }} />
          <div>Listas</div>
        </div>
        <div
          className={`bottom-nav-item ${selectedTab === 'favoritos' ? 'selected' : ''}`}
          onClick={() => handleTab('favoritos')}
        >
          <span style={{ fontSize: 22, color: selectedTab === 'favoritos' ? '#e53935' : '#bbb' }}>{selectedTab === 'favoritos' ? '❤️' : '🤍'}</span>
          <div>Favoritos</div>
        </div>
        <div
          className={`bottom-nav-item ${selectedTab === 'ajustes' ? 'selected' : ''}`}
          onClick={() => alert('Próximamente: Ajustes')}
        >
          <span style={{ fontSize: 22 }}>⚙️</span>
          <div>Ajustes</div>
        </div>
      </nav>
    </div>
  );
};

export default PrincipalListas;
