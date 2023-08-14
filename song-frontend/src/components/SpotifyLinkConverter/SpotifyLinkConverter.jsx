import React from 'react';

const SpotifyLinkConverter = ({ spotifyUri }) => {
  const convertToOpenLink = (uri) => {
    const parts = uri.split(':');
    const entityType = parts[1];
    const entityId = parts[2];
    
    let linkType = '';
    switch (entityType) {
      case 'album':
        linkType = 'album';
        break;
      case 'artist':
        linkType = 'artist';
        break;
      case 'track':
        linkType = 'track';
        break;
      default:
        return null;
    }
    
    return `https://open.spotify.com/${linkType}/${entityId}`;
  };

  return (
    <a href={convertToOpenLink(spotifyUri)} target="_blank" rel="noopener noreferrer">
      {spotifyUri}
    </a>
  );
};

export default SpotifyLinkConverter;
