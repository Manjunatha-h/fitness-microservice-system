import { Box, Button, FormControl, InputLabel, MenuItem, Select, TextField } from '@mui/material'
import React, { useState } from 'react'
import { addActivity } from '../services/api'


const ActivityForm = ({ onActivityAdded }) => {

    const [activity, setActivity] = useState({
        type: "RUNNING", duration: '', caloriesBurned: '',
        additionalMetrics: {}
    });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await addActivity(activity);
            onActivityAdded();
            setActivity({ type: "RUNNING", duration: '', caloriesBurned: '' });
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mb: 1 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
                <Select
                    value={activity.type}
                    onChange={(e) => setActivity({ ...activity, type: e.target.value })}
                    sx={{ backgroundColor: "rgba(255,255,255,0.96)", borderRadius: 1.5 }}>
                    <MenuItem value="RUNNING">Running</MenuItem>
                    <MenuItem value="WALKING">Walking</MenuItem>
                    <MenuItem value="CYCLING">Cycling</MenuItem>
                    <MenuItem value="SWIMMING">Swimming</MenuItem>
                    <MenuItem value="WEIGHT_TRAINING">Weight Training</MenuItem>
                    <MenuItem value="YOGA">Yoga</MenuItem>
                    <MenuItem value="HIIT">HIIT</MenuItem>
                    <MenuItem value="CARDIO">Cardio</MenuItem>
                    <MenuItem value="STRETCHING">Stretching</MenuItem>
                </Select>
            </FormControl>
            <TextField fullWidth
                label="Duration (Minutes)"
                type='number'
                sx={{ mb: 2, backgroundColor: "rgba(255,255,255,0.96)", borderRadius: 1.5 }}
                value={activity.duration}
                onChange={(e) => setActivity({ ...activity, duration: e.target.value })} />

            <TextField fullWidth
                label="Calories Burned"
                type='number'
                sx={{ mb: 2, backgroundColor: "rgba(255,255,255,0.96)", borderRadius: 1.5 }}
                value={activity.caloriesBurned}
                onChange={(e) => setActivity({ ...activity, caloriesBurned: e.target.value })} />

            <Button type='submit' variant='contained' sx={{ textTransform: "none", fontWeight: 700, px: 2.5 }}>
                Add Activity
            </Button>
        </Box>
    )
}

export default ActivityForm