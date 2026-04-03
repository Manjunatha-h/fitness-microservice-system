import { Card, CardContent, Grid2, Typography } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router';
import { getActivities } from '../services/api';

const ActivityList = ({ refreshKey }) => {
  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();

  const fetchActivities = async () => {
    try {
      const response = await getActivities();
      setActivities(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, [refreshKey]);

  return (
    <Grid2 container spacing={2}>
      {activities.map((activity) => (
        <Grid2 key={activity.id} size={{ xs: 12, md: 6 }}>
          <Card
            sx={{
              cursor: 'pointer',
              borderRadius: 3,
              border: '1px solid rgba(255,255,255,0.18)',
              background: 'linear-gradient(160deg, rgba(255,255,255,0.94) 0%, rgba(243,250,255,0.95) 100%)',
              transition: 'transform 0.2s ease, box-shadow 0.2s ease',
              '&:hover': {
                transform: 'translateY(-3px)',
                boxShadow: '0 14px 30px rgba(6, 17, 28, 0.18)',
              },
            }}
            onClick={() => navigate(`/activities/${activity.id}`)}
          >
            <CardContent>
              <Typography variant='h6' sx={{ fontWeight: 700, color: '#102a43' }}>{activity.type}</Typography>
              <Typography sx={{ color: '#334e68' }}>Duration: {activity.duration} min</Typography>
              <Typography sx={{ color: '#334e68' }}>Calories: {activity.caloriesBurned}</Typography>
            </CardContent>
          </Card>
        </Grid2>
      ))}
      {activities.length === 0 && (
        <Grid2 size={{ xs: 12 }}>
          <Typography sx={{ color: 'rgba(230,247,255,0.85)' }}>
            No activities yet. Add your first workout from the panel.
          </Typography>
        </Grid2>
      )}
    </Grid2>
  )
}

export default ActivityList