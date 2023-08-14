import React from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import SpotifyLinkConverter from '../SpotifyLinkConverter/SpotifyLinkConverter';
import { deleteSongById } from '../../service/SongService';

const SongList = ({ songMetadataList }) => {

  const handleDelete = async (id) => {
    try {
      await deleteSongById(id);
      // You might want to refresh the list after deleting
    } catch (error) {
      // Handle the error here
      console.error('Error deleting song:', error);
    }
  };


  return (
    <Box display='flex' flexDirection='column' alignItems='center' width='100%' padding={2} marginTop={2}>
      {songMetadataList.map((item) => (
        <Card key={item.id} sx={{ width: 900, backgroundColor: 'white', whiteSpace: 'pre-wrap', margin: 2 }}>
          <CardHeader
            title={
              <Typography variant="h5" component="div">
                {item.name}
              </Typography>
            }
            subheader={
              <Typography variant="subtitle1">
                Artists: {item.artists.map((artist) => artist.name).join(', ')}
              </Typography>
            }
          />

          <CardContent>
            <TableContainer>
              <Table>
                <TableHead>
                <TableRow>
                    <TableCell>Song ID</TableCell>
                    <TableCell>{item.id}</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell>Album Name</TableCell>
                    <TableCell>{item.album.name}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Album Spotify URI</TableCell>
                    <TableCell>
                      <Typography>
                        <SpotifyLinkConverter spotifyUri={item.album.spotifyUri} />
                      </Typography>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Spotify ID</TableCell>
                    <TableCell>{item.spotifyId}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Track Number</TableCell>
                    <TableCell>{item.trackNumber}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Disc Number</TableCell>
                    <TableCell>{item.discNumber}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Spotify URI</TableCell>
                    <TableCell>{item.spotifyUri}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Duration</TableCell>
                    <TableCell>{item.durationMs} ms</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Explicit</TableCell>
                    <TableCell>{item.explicit ? 'Yes' : 'No'}</TableCell>
                  </TableRow>
                  
                  <TableRow>
                    <TableCell>Album Spotify ID</TableCell>
                    <TableCell>{item.album.spotifyId}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Release Date</TableCell>
                    <TableCell>{item.album.releaseDate}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>Delete Song</TableCell>
                    <TableCell>
                      <Button variant="contained" color="secondary" onClick={() => handleDelete(item.id)}>
                        Delete
                      </Button>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      ))}
    </Box>
  );
};

export default SongList;
