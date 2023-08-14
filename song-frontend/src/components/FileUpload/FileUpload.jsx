import React, { useState } from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  TextField,
} from '@mui/material';
import { uploadFile } from '../../service/FileService';
import { useNavigate } from 'react-router-dom';

const FileUpload = ({ username }) => {
  const navigate = useNavigate()
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (selectedFile) {
      try {
        await uploadFile(username, selectedFile);
        console.log('File uploaded successfully');
        navigate("/home")

      } catch (error) {
        // Handle upload error
        console.error('Error uploading file:', error);
      }
    }
  };

  const handleCancel = () => {
    navigate("/home")
  }

  return (
    <Box display='flex' flexDirection='column' alignItems='center' padding={2} marginTop={2}>
        <Card sx={{ width: 900, backgroundColor: 'white', whiteSpace: 'pre-wrap', margin: 2 }}>
        <CardHeader title="Upload File" />
        <CardContent>
            <Box>
            <TextField
                type="file"
                onChange={handleFileChange}
                fullWidth
            />
            </Box>
            <Box mt={2}>
            <Button variant="contained" color="primary" onClick={handleUpload}>
                Upload
            </Button>
            </Box>
            <Box mt={2}>
            <Button variant="outlined" color="primary" onClick={handleCancel}>
                Cancel
            </Button>
            </Box>
        </CardContent>
        </Card>
    </Box>
  );
};

export default FileUpload;
