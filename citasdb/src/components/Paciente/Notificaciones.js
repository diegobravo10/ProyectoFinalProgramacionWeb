import React from 'react';

const GuiaWhatsAppConImagen = () => {
  return (
    <div style={{ 
      display: 'flex', 
      flexDirection: 'column', 
      alignItems: 'center', 
      justifyContent: 'center',
      textAlign: 'center',
      gap: 15,
      padding: 20,
      maxWidth: 300,
      margin: '0 auto'
    }}>
      <h3>Escanea el código QR</h3>
      
      <img 
        src="/imagen/codigoqr.png" 
        alt="Código QR WhatsApp" 
        style={{ width: 200, height: 200, objectFit: 'contain' }} 
      />
      
      <div style={{ textAlign: 'left', width: '100%' }}>
        <h4>Pasos para poder recibir notificaciones via WhatsApp:</h4>
        <ol>
          <li>Abre la cámara de tu celular.</li>
          <li>Apunta al código QR mostrado.</li>
          <li>Toca la notificación para abrir WhatsApp.</li>
          <li>Envía el mensaje por defecto "<strong>join too-avoid</strong>".</li>
          <li>Recibirá un mensaje, dar click en <strong>confirmar</strong>.</li>
          <li>Listo, proceso completado.</li>
        </ol>
      </div>
    </div>
  );
};

export default GuiaWhatsAppConImagen;
