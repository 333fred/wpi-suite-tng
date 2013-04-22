

var data = [
		
		{label: 'Getting Started',
		folder: false,
		id: 2
		},
		{label: 'Requirements',
		folder: true,
		id: 3,
		children: [{
						label: 'Adding a Requirement',
						folder: false,
						id: 30
					}, {
						label: 'Editing a Requirement',
						folder: false,
						id: 31
					},{
						label: 'Deleting a Requirement',
						folder: false,
						id: 32
					}, {
						label: 'Notes, Logs, and Users',
						folder: false,
						id: 33
					}, {
				    	label: 'Tasks',
				    	folder: false,
				    	id: 34
				    }, {
				    	label: 'Acceptance Tests',
				    	folder: false,
				    	id: 35
			    }
		]},
		{label: 'Iterations',
    	folder: true,
    	id: 4,
    	children: [{
                    label : 'Creating an Iteration',
                    folder : false,
                    id : 40
                }, {
		        	label: 'Editing an Iteration',
		        	folder: false,
		        	id: 41
			    }, {
		        	label: 'Adding a Requirement to an Iteration',
		        	folder: false,
		        	id: 42
			    }]
            },
        {label: 'Filters',
        folder: true,
        id: 5,
    	children: [{
    				label : 'Creating Filters',
                    folder : false,
                    id : 50
    			}, {
    				label : 'Using Filters',
    				folders : false,
    				id : 51
    			}]
    		},   
        {label: 'Viewing Statistics',
    	folder: false,
    	id: 6
    	},
    	{label: 'Editing Estimates',
    	folder: false,
    	id: 7
    	},
    	{label: 'Sort Requirements',
    	folder: false,
    	id: 8
    	},
    	{label: 'Closing Tabs',
    	folder: false,
    	id: 9
    	},
    	{label: 'Admin',
		folder: true,
		id: 1,
		children: [{
						label: 'Creating Users',
						folder: false,
						id: 10
					},  {label: 'Deleting Users',
						folder: false,
						id: 11
					},  {label: 'Create Project',
						folder: false,
						id: 12
					},  {label: 'Deleting Projects',
						folder: false,
						id: 13
					},  {label: 'Adding Users',
						folder: false,
						id: 14
				}]
			},
		
    ]
