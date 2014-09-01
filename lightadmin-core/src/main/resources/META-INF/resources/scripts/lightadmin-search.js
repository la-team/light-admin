/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
function ScopesComponent( scopesContainerId, searcher, domainRestScopeBaseUrl ) {

	this.domainRestScopeBaseUrl = domainRestScopeBaseUrl;

	this.searcher = searcher;
	this.searcher.addScopesComponent( this );

	this.scopesContainer = $( scopesContainerId );

	var instance = this;

	this.domainRestScopeSearchCountBaseUrl = function ( scopeName, searchCriteria ) {
		return this.domainRestScopeBaseUrl + '/' + scopeName + '/search/count' + (searchCriteria != null ? '?' + searchCriteria : '');
	};

	this.onScopeActivated = function ( event ) {
		event.preventDefault();

		var scopeName = $( this ).attr( 'scope-name' );
		instance.activateScope( scopeName );

		instance.searcher.search();
	};

	this.activateScope = function ( scopeName ) {
		$( "a.scope.active", this.scopesContainer ).removeClass( "active green" ).addClass( "blue" );
		$( "a.scope[scope-name='" + scopeName + "']", this.scopesContainer ).addClass( "active green" ).removeClass( "blue" );
	};

	this.getActiveScopeName = function () {
		return $( "a.scope.active", this.scopesContainer ).attr( 'scope-name' );
	};

	this.refreshScopesTotalRecords = function ( searchCriteria ) {
		$( "a.scope", this.scopesContainer ).each( function () {
			var scope = $( this );
			var scopeName = scope.attr( 'scope-name' );

			$.ajax( {
						type: 'GET',
						url: instance.domainRestScopeSearchCountBaseUrl( scopeName, searchCriteria ),
						dataType: 'json',
						success: function ( totalRecords ) {
							scope.html( scopeName + ' (' + totalRecords + ')' );
						}
					} );
		} );
	};

	$( "a.scope", this.scopesContainer ).click( this.onScopeActivated );
}

function FilterComponent( filterFormContainerName, searcher ) {

	this.searcher = searcher;
	this.searcher.addFilterComponent( this );

	this.filterFormContainer = $( "form[name='" + filterFormContainerName + "']" );

	var instance = this;

	this.submitForm = function ( event ) {
		event.preventDefault();

		instance.searcher.search();
	};

	this.getSearchCriteria = function () {
		return instance.filterFormContainer.serialize();
	};

	this.resetSearchCriteria = function () {
		instance.filterFormContainer[0].reset();

		$( ".chzn-select" ).trigger( "liszt:updated" );

		$.uniform.update();

		instance.searcher.search();
	};

	$( "#reset-filter", this.filterFormContainer ).click( this.resetSearchCriteria );

	this.filterFormContainer.submit( this.submitForm );

}

function Searcher( resourceName ) {

	this.domainRestScopeBaseUrl = ApplicationConfig.getDomainEntitySearchScopeRestUrl(resourceName);

	this.scopesComponent = null;
	this.filterComponent = null;
	this.dataTable = null;

	this.domainRestScopeSearchBaseUrl = function ( scopeName ) {
		return this.domainRestScopeBaseUrl + '/' + scopeName + '/search';
	};

	this.addScopesComponent = function ( scopesComponent ) {
		this.scopesComponent = scopesComponent;
	};

	this.addFilterComponent = function ( filterComponent ) {
		this.filterComponent = filterComponent;
	};

	this.addDataTable = function ( dataTable ) {
		this.dataTable = dataTable;
	};

	this.search = function () {
		if ( this.scopesComponent == null ) {
			this.dataTable.fnReloadAjax( this.domainRestScopeSearchBaseUrl( 'All' ) );
			return;
		}

		var activeScopeName = this.scopesComponent.getActiveScopeName();
		if ( this.filterComponent == null ) {
			this.dataTable.fnReloadAjax( this.domainRestScopeSearchBaseUrl( activeScopeName ) );
			return;
		}

		var searchCriteria = this.filterComponent.getSearchCriteria();
		this.dataTable.fnReloadAjax( this.domainRestScopeSearchBaseUrl( activeScopeName ) + '?' + searchCriteria );
	};

	this.onSearchCompleted = function () {
		if ( this.filterComponent != null ) {
			this.scopesComponent.refreshScopesTotalRecords( this.filterComponent.getSearchCriteria() );
		} else {
			this.scopesComponent.refreshScopesTotalRecords( null );
		}
	}
}

function createSearcher( resourceName ) {
    var searcher = new Searcher( resourceName );

	$( document ).data( 'lightadmin.searcher', searcher );

	return searcher;
}

function getSearcher() {
	return $( document ).data( 'lightadmin.searcher' );
}