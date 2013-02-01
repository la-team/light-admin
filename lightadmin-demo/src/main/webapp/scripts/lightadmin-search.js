function ScopesComponent( scopesContainerId, searcher ) {

	this.searcher = searcher;
	this.searcher.addScopesComponent(this);

	this.scopesContainer = $( scopesContainerId );

	var instance = this;

	this.onScopeActivated = function( event ) {
		event.preventDefault();

		var scopeName = $( this ).attr('scope-name');
		instance.activateScope(scopeName);

		instance.searcher.search();
	};

	this.activateScope = function( scopeName ) {
		$("a.scope.active", this.scopesContainer ).removeClass("active green").addClass("blue");
		$("a.scope[scope-name='" + scopeName + "']", this.scopesContainer).addClass("active green" ).removeClass("blue");
	};

	this.getActiveScopeName = function() {
		return $("a.scope.active", this.scopesContainer ).attr('scope-name');
	};

	this.setActiveScopeTotalRecords = function(totalRecordsReturned) {
		$("a.scope.active", this.scopesContainer ).html(this.getActiveScopeName() + ' (' + totalRecordsReturned + ')');
	};

	$("a.scope", this.scopesContainer ).click(this.onScopeActivated);
}

function FilterComponent( filterFormContainerName, searcher ) {

	this.searcher = searcher;
	this.searcher.addFilterComponent(this);

	this.filterFormContainer = $("form[name='" + filterFormContainerName + "']" );

	var instance = this;

	this.submitForm = function( event ) {
		event.preventDefault();

		instance.searcher.search();
	};

	this.getSearchCriteria = function() {
		return instance.filterFormContainer.serialize();
	};

	this.resetSearchCriteria = function() {
		instance.filterFormContainer[0].reset();

		$.uniform.update();

		instance.searcher.search();
	};

	$("#reset-filter", this.filterFormContainer ).click(this.resetSearchCriteria);

	this.filterFormContainer.submit(this.submitForm);

}

function Searcher( domainRestScopeBaseUrl ) {

	this.domainRestScopeBaseUrl = domainRestScopeBaseUrl;

	this.scopesComponent = null;
	this.filterComponent = null;
	this.dataTable = null;

	this.domainRestScopeSearchBaseUrl = function( scopeName ) {
		return this.domainRestScopeBaseUrl + '/' + scopeName + '/search';
	};

	this.addScopesComponent = function(scopesComponent) {
		this.scopesComponent = scopesComponent;
	};

	this.addFilterComponent = function(filterComponent) {
		this.filterComponent = filterComponent;
	};

	this.addDataTable = function(dataTable) {
		this.dataTable = dataTable;
	};

	this.search = function() {
		if (this.scopesComponent == null ) {
			this.dataTable.fnReloadAjax( this.domainRestScopeSearchBaseUrl('All') );
			return;
		}

		var activeScopeName = this.scopesComponent.getActiveScopeName();
		if ( this.filterComponent == null ) {
			this.dataTable.fnReloadAjax( this.domainRestScopeSearchBaseUrl(activeScopeName) );
			return;
		}

		var searchCriteria = this.filterComponent.getSearchCriteria();
		this.dataTable.fnReloadAjax( this.domainRestScopeSearchBaseUrl(activeScopeName) + '?' + searchCriteria );
	};

	this.onSearchCompleted = function( totalRecordsReturned ) {
		this.scopesComponent.setActiveScopeTotalRecords(totalRecordsReturned);
	}
}

function createSearcher( domainRestScopeBaseUrl ) {
	var searcher = new Searcher(domainRestScopeBaseUrl);
	$( document ).data('lightadmin.searcher', searcher );
	return searcher;
}

function getSearcher() {
	return $( document ).data('lightadmin.searcher' );
}